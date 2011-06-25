
package org.webframe.web.page.adapter.jdbc.spring.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.webframe.web.page.adapter.jdbc.util.StandardConnectionCreator;

/**
 * This connection creator uses spring to manage connection in transactional environment.
 * 
 * @author Stepan Marek
 * @version $Revision: 1.2 $ $Date: 2005/10/20 16:37:49 $
 * @see DataSourceUtils
 */
public class SpringConnectionCreator extends StandardConnectionCreator {

	public SpringConnectionCreator() {
	}

	public SpringConnectionCreator(DataSource dataSource) {
		super(dataSource);
	}

	public Connection createConnection() throws SQLException {
		return DataSourceUtils.getConnection(getDataSource());
	}

	public void close(ResultSet result, PreparedStatement statement, Connection connection) {
		JdbcUtils.closeResultSet(result);
		JdbcUtils.closeStatement(statement);
		// deprecated since spring 1.2
		DataSourceUtils.releaseConnection(connection, getDataSource());
	}
}