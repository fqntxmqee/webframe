/**
 * Copyright (c) 2003 held jointly by the individual authors.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *  > http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.adapter.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.DefaultListBackedValueList;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.AbstractValueListAdapter;
import org.webframe.web.page.adapter.jdbc.objectWrapper.ResultSetDecorator;
import org.webframe.web.page.adapter.jdbc.util.ConnectionCreator;
import org.webframe.web.page.adapter.jdbc.util.JdbcUtil;
import org.webframe.web.page.adapter.jdbc.util.SqlPagingSupport;
import org.webframe.web.page.adapter.jdbc.util.StandardConnectionCreator;
import org.webframe.web.page.adapter.jdbc.util.StandardStatementBuilder;
import org.webframe.web.page.adapter.jdbc.util.StatementBuilder;
import org.webframe.web.page.adapter.util.ObjectValidator;

/**
 * This adapter handles the standard functionality of creating a query and execution it... If you
 * set validator, it will use special ResultSetDecorator.
 * 
 * @see org.webframe.web.page.adapter.jdbc.objectWrapper.ResultSetDecorator
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.27 $ $Date: 2006/04/18 17:15:05 $
 */
public abstract class AbstractJdbcAdapter extends AbstractValueListAdapter {

	/** Commons logger. */
	private static final Log	LOGGER				= LogFactory.getLog(AbstractJdbcAdapter.class);

	/** The sql to execute. */
	private String					sql;

	/** Display generated sql on th standard output */
	private boolean				showSql				= false;

	/** The StatementBuilder to help generate a sql query. */
	private StatementBuilder	statementBuilder	= new StandardStatementBuilder();

	/** The ConnectionCreator to help SQL connection handling */
	private ConnectionCreator	connectionCreator	= new StandardConnectionCreator();

	/**
	 * The validator for ResultSet's records.
	 */
	private ObjectValidator		_validator			= null;

	/**
	 * Helper to provide true in-database paging.
	 */
	private SqlPagingSupport	sqlPagingSupport;

	public AbstractJdbcAdapter() {
	}

	/**
	 * @see org.webframe.web.page.ValueListAdapter#getValueList(java.lang.String,
	 *      org.webframe.web.page.ValueListInfo)
	 */
	public ValueList getValueList(String name, ValueListInfo info) {
		if (info.getSortingColumn() == null) {
			info.setPrimarySortColumn(getDefaultSortColumn());
			info.setPrimarySortDirection(getDefaultSortDirectionInteger());
		}
		int numberPerPage = info.getPagingNumberPer();
		if (numberPerPage == Integer.MAX_VALUE) {
			numberPerPage = getDefaultNumberPerPage();
			info.setPagingNumberPer(numberPerPage);
		}
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			boolean doSqlPaging = ((getAdapterType() & DO_PAGE) == 0);
			connection = connectionCreator.createConnection();
			StringBuffer query = (sqlPagingSupport != null) ? sqlPagingSupport.getPagedQuery(sql) : new StringBuffer(sql);
			statement = statementBuilder.generate(connection, query, info.getFilters(), sqlPagingSupport == null
						&& doSqlPaging);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(query.toString());
			}
			if (showSql) {
				System.out.println("sql: " + query.toString());
			}
			result = getResultSet(statement, info);
			if (sqlPagingSupport != null) {
				PreparedStatement countStatement = null;
				ResultSet countResult = null;
				try {
					StringBuffer countQuery = sqlPagingSupport.getCountQuery(sql);
					countStatement = statementBuilder.generate(connection, countQuery, info.getFilters(), false);
					if (showSql) {
						System.out.println("count sql: " + countQuery.toString());
					}
					countResult = countStatement.executeQuery();
					if (countResult.next()) {
						info.setTotalNumberOfEntries(countResult.getInt(1));
					}
				} finally {
					JdbcUtil.close(countResult, countStatement, null);
				}
			} else if (doSqlPaging) {
				result.last();
				int totalRows = result.getRow();
				info.setTotalNumberOfEntries(totalRows);
				if (numberPerPage == 0) {
					numberPerPage = getDefaultNumberPerPage();
				}
				int pageNumber = info.getPagingPage();
				if (pageNumber > 1) {
					if ((pageNumber - 1) * numberPerPage > totalRows) {
						pageNumber = ((totalRows - 1) / numberPerPage) + 1;
						info.setPagingPage(pageNumber);
					}
				}
				if (pageNumber > 1) {
					result.absolute((pageNumber - 1) * numberPerPage);
				} else {
					result.beforeFirst();
				}
			}
			List<Object> list = processResultSet(name, result, (doSqlPaging) ? numberPerPage : Integer.MAX_VALUE, info);
			if (!doSqlPaging) {
				info.setTotalNumberOfEntries(list.size());
			}
			return new DefaultListBackedValueList(list, info);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		} finally {
			connectionCreator.close(result, statement, connection);
		}
	}

	/**
	 * @param statement
	 * @param info This info will be set to validator.
	 * @return ResultSet (validator is null) or ResultSetDecorator (validator is not null)
	 * @throws SQLException
	 * @see org.webframe.web.page.adapter.util.ObjectValidator
	 * @see org.webframe.web.page.adapter.jdbc.objectWrapper.ResultSetDecorator
	 */
	private ResultSet getResultSet(PreparedStatement statement, ValueListInfo info) throws SQLException {
		if (_validator == null) {
			return statement.executeQuery();
		} else {
			_validator.setValueListInfo(info);
			return new ResultSetDecorator(statement.executeQuery(), _validator);
		}
	}

	/**
	 * This method takes the result and puts the VOs in the List.
	 * 
	 * @param result The ResultSet to interate through.
	 * @param info is ussually constant during this process, you can use it for passing additional
	 *           parameters from controler. (Like in DefaultWrapperAdapter)
	 * @return The List of VOs.
	 */
	public abstract List<Object> processResultSet(String name, ResultSet result, int numberPerPage, ValueListInfo info)
				throws SQLException;

	/**
	 * @return Returns the dataSource.
	 */
	public DataSource getDataSource() {
		return connectionCreator.getDataSource();
	}

	/**
	 * @param dataSource The dataSource to set.
	 */
	public void setDataSource(DataSource dataSource) {
		connectionCreator.setDataSource(dataSource);
	}

	/**
	 * @return Returns the sql.
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql The sql to set.
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return Returns the statementBuilder.
	 */
	public StatementBuilder getStatementBuilder() {
		return statementBuilder;
	}

	/**
	 * @param statementBuilder The statementBuilder to set.
	 */
	public void setStatementBuilder(StatementBuilder statementBuilder) {
		this.statementBuilder = statementBuilder;
	}

	/**
	 * @return Returns the showSql.
	 */
	public boolean isShowSql() {
		return showSql;
	}

	/**
	 * @param showSql The showSql to set.
	 */
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	/**
	 * @return Returns the objectValidator.
	 */
	public ObjectValidator getValidator() {
		return _validator;
	}

	/**
	 * <p> If is set to not null value, it uses a special <code>ResultsSetDecorator<code>, that
	 * enable or disable filtering objects in the final valuelist. </p> <h4>NOTE:</h4> <p> It
	 * respects the total count of entries that overlap your paged list. Simply spoken it supports
	 * features such as paging. </p>
	 * 
	 * @param objectValidator The objectValidator to set. The null value means that validator is
	 *           disabled.
	 * @see org.webframe.web.page.adapter.util.ObjectValidator
	 * @see org.webframe.web.page.adapter.jdbc.objectWrapper.ResultSetDecorator
	 */
	public void setValidator(ObjectValidator objectValidator) {
		this._validator = objectValidator;
	}

	/**
	 * @return Returns the connectionCreator.
	 */
	public ConnectionCreator getConnectionCreator() {
		return connectionCreator;
	}

	/**
	 * @param connectionCreator The connectionCreator to set.
	 */
	public void setConnectionCreator(ConnectionCreator connectionCreator) {
		this.connectionCreator = connectionCreator;
	}

	public SqlPagingSupport getSqlPagingSupport() {
		return sqlPagingSupport;
	}

	public void setSqlPagingSupport(SqlPagingSupport sqlPagingSupport) {
		this.sqlPagingSupport = sqlPagingSupport;
	}
}