
package org.webframe.web.page.adapter.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.webframe.web.page.DefaultListBackedValueList;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.AbstractValueListAdapter;
import org.webframe.web.page.adapter.hibernate.util.ScrollableResultsDecorator;
import org.webframe.web.page.adapter.hibernate.util.StatementBuilder;
import org.webframe.web.page.adapter.util.ObjectValidator;

/**
 * This adapter wraps the functionality of Hibernate. Add extra functionality such as paging,
 * focusing and validating of current result set. <i> "Hibernate is a powerful, ultra-high
 * performance object/relational persistence and query service for Java. Hibernate lets you develop
 * persistent classes following common Java idiom - including association, inheritance,
 * polymorphism, composition and the Java collections framework. The Hibernate Query Language,
 * designed as a "minimal" object-oriented extension to SQL, provides an elegant bridge between the
 * object and relational worlds. Hibernate also allows you to express queries using native SQL or
 * Java-based Criteria and Example queries. Hibernate is now the most popular object/relational
 * mapping solution for Java." </i> -http://www.hibernate.org/
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午02:05:56
 */
public class HibernateAdapter extends AbstractValueListAdapter {

	/** The Hibernate SessionFactory. */
	private SessionFactory		sessionFactory;

	/**
	 * <p> If is set, it use special ScrollableResultsDecorator, that enable or disable to add object
	 * in final list. </p> <h4>NOTE:</h4> <p> Also, it respects the total count of entries that
	 * overlap your paged list. </p>
	 */
	private ObjectValidator		_validator								= null;

	/** Commons logger. */
	private static final Log	LOGGER									= LogFactory.getLog(HibernateAdapter.class);

	/** If a new Session should be created if no thread-bound found. */
	private boolean				allowCreate								= true;

	/** The hibernate query. */
	private String					hql;

	private String					namedQuery;

	/**
	 * The max rows in ResulSet to doFocus
	 * 
	 * @author Andrej Zachar
	 */
	private long					maxRowsForFocus						= Long.MAX_VALUE;

	/**
	 * The name of object use to get focus property in hibernate sql syntax SELECT
	 * defaultFocusPropertyObjectAlias.getFocusProperty ...
	 * 
	 * @author Andrej Zachar
	 */
	private String					defaultFocusPropertyObjectAlias	= "";

	/**
	 * Enable or Disable String length checking of given filters values. If filter value is null or
	 * empty is removed from query.
	 * 
	 * @author Andrej Zachar
	 */
	private boolean				_isRemoveEmptyStrings				= false;

	private StatementBuilder	statementBuilder;

	/**
	 * Enable or disable optimalization of the query for focus property.
	 */
	private boolean				_focusOptimalization					= true;

	/**
	 * @return Returns the focusOptimalization.
	 */
	public boolean isFocusOptimalization() {
		return _focusOptimalization;
	}

	/**
	 * Enable or disable optimalization of the query for focus property.
	 * 
	 * @param focusOptimalization true - enable query with short select, false - query with full
	 *           select
	 */
	public void setFocusOptimalization(boolean focusOptimalization) {
		_focusOptimalization = focusOptimalization;
	}

	/**
	 * <p> If is set, it use special ScrollableResultsDecorator, that enable or disable to add object
	 * in final list. </p> <h4>NOTE:</h4> <p> Also, it respects the total count of entries that
	 * overlap your paged list. </p>
	 * 
	 * @param validator The validator to set.
	 */
	public void setValidator(ObjectValidator validator) {
		_validator = validator;
	}

	/**
	 * @return Returns the isPrefilterEmpty.
	 */
	public boolean isRemoveEmptyStrings() {
		return _isRemoveEmptyStrings;
	}

	/**
	 * Enable or Disable String length checking of given filters values. If filter value is null or
	 * empty is removed from query.
	 * 
	 * @param isPrefilterEmpty true-remove null and empty, false - remove only null filters.
	 */
	public void setRemoveEmptyStrings(boolean isPrefilterEmpty) {
		_isRemoveEmptyStrings = isPrefilterEmpty;
	}

	/**
	 * @see org.webframe.web.page.ValueListAdapter#getValueList(java.lang.String,
	 *      org.webframe.web.page.ValueListInfo)
	 */
	@SuppressWarnings("unchecked")
	public ValueList getValueList(String name, ValueListInfo info) {
		LOGGER.debug("getValueList(String, ValueListInfo) - start");
		if (info.getSortingColumn() == null) {
			info.setPrimarySortColumn(getDefaultSortColumn());
			info.setPrimarySortDirection(getDefaultSortDirectionInteger());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("The default sort column '"
							+ getDefaultSortColumn()
							+ "' with direction '"
							+ getDefaultSortDirectionInteger()
							+ "' was  set.");
			}
		}
		int numberPerPage = info.getPagingNumberPer();
		if (numberPerPage == Integer.MAX_VALUE) {
			numberPerPage = getDefaultNumberPerPage();
			info.setPagingNumberPer(numberPerPage);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("The paging number per page '" + numberPerPage + "' was  set.");
			}
		}
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), allowCreate);
		try {
			Query query;
			boolean doFocus = ((getAdapterType() & DO_FOCUS) == 0)
						&& info.isFocusEnabled()
						&& info.isDoFocus()
						&& (namedQuery == null);
			if (doFocus) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Start to focusing adapterName '" + name + "', ValueListInfo info = " + info + "'");
				}
				ScrollableResults results = getScrollableResults(getQueryForFocus(info, session), info);
				results.beforeFirst();
				doFocusFor(info, results);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Focusing finished for adapterName '" + name + "', ValueListInfo info '" + info + "'");
				}
			}
			query = getQuery(info, session);
			boolean doPaging = ((getAdapterType() & DO_PAGE) == 0);
			List<Object> list;
			if (doPaging) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("getValueList(String adapterName = "
								+ name
								+ ", ValueListInfo info = "
								+ info
								+ ") - Start to paging result set");
				}
				list = new ArrayList<Object>(numberPerPage);
				ScrollableResults results = getScrollableResults(query, info);
				results.last();
				int lastRowNumber = results.getRowNumber();
				info.setTotalNumberOfEntries(lastRowNumber + 1);
				if (numberPerPage == 0) {
					numberPerPage = getDefaultNumberPerPage();
				}
				int pageNumber = info.getPagingPage();
				boolean isResult;
				if (pageNumber > 1) {
					if ((pageNumber - 1) * numberPerPage > lastRowNumber) {
						pageNumber = (lastRowNumber / numberPerPage) + 1;
						info.setPagingPage(pageNumber);
					}
				}
				// fixed by liujuan 2008.6.5
				isResult = results.first();
				if (pageNumber > 1) {
					// isResult = results.scroll((pageNumber - 1) * numberPerPage - lastRowNumber);
					isResult = results.scroll((pageNumber - 1) * numberPerPage);
				}
				/*else
				{
				   isResult = results.first();
				}*/
				for (int i = 0; i < numberPerPage && isResult; i++) {
					list.add(results.get(0));
					isResult = results.next();
				}
				LOGGER.debug("Sorting finished.");
			} else {
				LOGGER.debug("Retrieving a list directly from the query.");
				list = query.list();
				info.setTotalNumberOfEntries(list.size());
			}
			ValueList returnValueList = getListBackedValueList(info, list);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Retrieved list was wrapped in valuelist, info=" + info);
			}
			return returnValueList;
		} catch (HibernateException e) {
			LOGGER.error("Error getting data in adapater '" + name + "' with info = '" + info + "'", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		} catch (Exception e) {
			LOGGER.fatal("Fatal error getting data in adapater '" + name + "' with info = '" + info + "'", e);
			return null;
		} finally {
			SessionFactoryUtils.releaseSession(session, getSessionFactory());
		}
	}

	/**
	 * @param info
	 * @param list
	 * @return DefaultListBackValueList instance
	 */
	protected ValueList getListBackedValueList(ValueListInfo info, List<Object> list) {
		return new DefaultListBackedValueList(list, info);
	}

	/**
	 * @param info
	 * @param results
	 * @throws HibernateException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void doFocusFor(ValueListInfo info, ScrollableResults results) throws HibernateException {
		info.setFocusStatus(ValueListInfo.FOCUS_NOT_FOUND);
		int currentRow;
		if (isFocusOptimalization()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Focusing only property '" + info.getFocusProperty() + "' == '" + info.getFocusValue() + "'.");
			}
			for (currentRow = 0; ((results.next()) && (currentRow < maxRowsForFocus)); currentRow++) {
				String value = results.get(0).toString();
				if (value.equalsIgnoreCase(info.getFocusValue())) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Focus property '" + info.getFocusProperty() + "' in row '" + currentRow + "'.");
					}
					info.setPagingPageFromRowNumber(results.getRowNumber());
					info.setFocusedRowNumberInTable(results.getRowNumber());
					info.setFocusStatus(ValueListInfo.FOCUS_FOUND);
					break;
				}
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Focusing object with the property '"
							+ info.getFocusProperty()
							+ "' == '"
							+ info.getFocusValue()
							+ "'.");
			}
			for (currentRow = 0; ((results.next()) && (currentRow < maxRowsForFocus)); currentRow++) {
				Object value;
				try {
					value = PropertyUtils.getProperty(results.get(0), info.getFocusProperty());
				} catch (HibernateException e) {
					LOGGER.error("Error getting focus property '" + info.getFocusProperty() + "'", e);
					throw e;
				} catch (Exception e) {
					LOGGER.warn("Ingoring error while getting focus property '" + info.getFocusProperty() + "'", e);
					continue;
				}
				if (value.toString().equalsIgnoreCase(info.getFocusValue())) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Focus object's property '"
									+ info.getFocusProperty()
									+ "' was found in the row '"
									+ currentRow
									+ "'.");
					}
					info.setPagingPageFromRowNumber(results.getRowNumber());
					info.setFocusedRowNumberInTable(results.getRowNumber());
					info.setFocusStatus(ValueListInfo.FOCUS_FOUND);
					break;
				}
			}
		}
		if (currentRow == maxRowsForFocus) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Focus for property '"
							+ info.getFocusProperty()
							+ "' exceded maximum rows for focus '"
							+ maxRowsForFocus
							+ "'.");
			}
			info.setFocusStatus(ValueListInfo.FOCUS_TOO_MANY_ITEMS);
		}
	}

	/**
	 * @param query
	 * @param info ValueListInfo This info will be set to validator.
	 * @return ScrollableResults, if is set non null _validator, it returns the
	 *         ScrollableResultsDecorator.
	 * @throws HibernateException
	 */
	private ScrollableResults getScrollableResults(Query query, ValueListInfo info) throws HibernateException {
		ScrollableResults results;
		if (_validator == null) {
			LOGGER.debug("Validator is null, using normal ScrollableResults");
			results = query.scroll();
		} else {
			LOGGER.info("Using decorator of the ScrollableResults with your validator.");
			_validator.setValueListInfo(info);
			results = new ScrollableResultsDecorator(query.scroll(), _validator);
		}
		return results;
	}

	/**
	 * @param info
	 * @param session
	 * @return @throws HibernateException
	 */
	private Query getQuery(ValueListInfo info, Session session) throws HibernateException, ParseException {
		if (getHql() != null) {
			return getStatementBuilder().generate(session, new StringBuffer(getHql()), info.getFilters(),
				_isRemoveEmptyStrings);
		} else {
			if (namedQuery != null) {
				return session.getNamedQuery(getNamedQuery());
			} else {
				throw new HibernateException("Please define any QUERY in value list retrieve adpater!");
			}
		}
	}

	/**
	 * If focus optimalization is true, it select only focus property. For validator is recommended
	 * to set it to false, while you want to validate properties of retrieved objects.
	 * 
	 * @param info
	 * @param session
	 * @return
	 * @throws HibernateException
	 */
	private Query getQueryForFocus(ValueListInfo info, Session session) throws HibernateException, ParseException {
		if (isFocusOptimalization()) {
			LOGGER.info("Focus will use optimalizated query.");
			return getOptimizedQuery(info, session);
		} else {
			LOGGER.info("Focus will use normal (full) query.");
			return getQuery(info, session);
		}
	}

	/**
	 * @param info
	 * @param session
	 * @return query that select only focus property.
	 * @throws HibernateException
	 * @throws ParseException
	 */
	private Query getOptimizedQuery(ValueListInfo info, Session session) throws HibernateException, ParseException {
		if (getHql() != null) {
			return getStatementBuilder().generateForFocus(session, new StringBuffer(getHql()), info.getFilters(),
				_isRemoveEmptyStrings, defaultFocusPropertyObjectAlias, info.getFocusProperty());
		} else {
			throw new HibernateException("Please define any HQL QUERY in value list retrieve adpater, function is not implemented for NamedQuery!");
		}
	}

	/**
	 * Set the Hibernate SessionFactory to be used by this DAO.
	 * 
	 * @param sessionFactory The Hibernate SessionFactory to be used by this DAO.
	 */
	public final void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Return the Hibernate SessionFactory used by this DAO.
	 * 
	 * @return The Hibernate SessionFactory used by this DAO.
	 */
	protected final SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * Sets the hql used to retrieve the data.
	 * 
	 * @param hql The hql to set.
	 * @deprecated use setHql(String)
	 */
	public void setHsql(String hql) {
		this.hql = hql;
	}

	/**
	 * Sets the hql used to retrieve the data.
	 * 
	 * @param hql The hql to set.
	 */
	public void setHql(String hql) {
		this.hql = hql;
	}

	/**
	 * Returns the namedQuery.
	 * 
	 * @return Returns the namedQuery.
	 */
	public String getNamedQuery() {
		return namedQuery;
	}

	/**
	 * Sets the namedQuery. <p> NOTE: by using this you can not enable sorting, filtering, paging of
	 * the data, and focusing of rows. </p>
	 * 
	 * @param namedQuery The namedQuery to set.
	 */
	public void setNamedQuery(String namedQuery) {
		this.namedQuery = namedQuery;
	}

	/**
	 * Gets the hql used to retrieve the data.
	 * 
	 * @return Returns the hql.
	 */
	public String getHql() {
		return hql;
	}

	/**
	 * Sets: If a new Session should be created if no thread-bound found.
	 * 
	 * @param allowCreate The allowCreate to set.
	 */
	public void setAllowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
	}

	/**
	 * Maximum rows to search with Focus
	 * 
	 * @return Returns the maxRowsForFocus.
	 */
	public long getMaxRowsForFocus() {
		return maxRowsForFocus;
	}

	/**
	 * Maximum rows to search with Focus
	 * 
	 * @param maxRowsForFocus The maxRowsForFocus to set.
	 */
	public void setMaxRowsForFocus(long maxRowsForFocus) {
		this.maxRowsForFocus = maxRowsForFocus;
	}

	/**
	 * @return Returns the defaultFocusPropertyObject.
	 */
	public String getDefaultFocusPropertyObjectAlias() {
		return defaultFocusPropertyObjectAlias;
	}

	/**
	 * The name of object use to get focus property in hibernate hql syntax SELECT
	 * defaultFocusPropertyObjectAlias.getFocusProperty ...
	 * 
	 * @param defaultFocusPropertyObjectAlias The defaultFocusPropertyObjectAlias to set.
	 */
	public void setDefaultFocusPropertyObjectAlias(String defaultFocusPropertyObjectAlias) {
		this.defaultFocusPropertyObjectAlias = defaultFocusPropertyObjectAlias + ".";
	}

	/**
	 * @return Returns the statementBuilder.
	 */
	public StatementBuilder getStatementBuilder() {
		if (statementBuilder == null) {
			statementBuilder = new StatementBuilder();
		}
		return statementBuilder;
	}

	/**
	 * @param statementBuilder The statementBuilder to set.
	 */
	public void setStatementBuilder(StatementBuilder statementBuilder) {
		this.statementBuilder = statementBuilder;
	}
}