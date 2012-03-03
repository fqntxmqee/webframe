
package org.webframe.core.datasource;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.webframe.core.util.SqlScriptsUtils;

/**
 * 数据源工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-17
 *          下午02:23:51
 */
public abstract class DataSourceUtil {

	private static final Log		log					= LogFactory.getLog(DataSourceUtil.class);

	private static WFDataSource	defaultDataSource	= null;

	static void defaultDataSource(WFDataSource ds) {
		defaultDataSource = ds;
	}

	/**
	 * 执行sql脚本
	 * 
	 * @param initScripts
	 * @param ds
	 * @throws SQLException
	 * @throws IOException
	 * @author: 黄国庆 2011-1-17 下午02:32:31
	 */
	public static void executeSqlScripts(Reader initScripts, DataSource ds) throws SQLException, IOException {
		String sqlString = IOUtils.toString(initScripts);
		Map<String, String> sqlMap = SqlScriptsUtils.analyzeSqlFile(sqlString);
		if (DataSourceUtil.getDataBaseType() != DataBaseType.HSQLDB) {
			SqlScriptsUtils.executeBatchSql(sqlMap, ds);
		} else {
			SqlScriptsUtils.executeSql(sqlMap, ds);
		}
	}

	/**
	 * 执行sql脚本
	 * 
	 * @param initScripts
	 * @param ds
	 * @throws SQLException
	 * @throws IOException
	 * @author: 黄国庆 2011-1-17 下午02:32:31
	 */
	public static void executeSqlScripts(Reader initScripts) throws SQLException, IOException {
		Assert.notNull(defaultDataSource, "默认数据源不存在！");
		executeSqlScripts(initScripts, defaultDataSource);
	}

	public static DataBaseType getDataBaseType() {
		Assert.notNull(defaultDataSource, "默认数据源不存在！");
		return defaultDataSource.getDatabaseType();
	}

	public static DataBaseType getDataBaseType(DataSource ds) {
		if (ds instanceof WFDataSource) {
			return ((WFDataSource) ds).getDatabaseType();
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
			DatabaseMetaData info = conn.getMetaData();
			String databaseProductName = info.getDatabaseProductName();
			String lowerCase = databaseProductName.replaceAll(" ", "").toLowerCase();
			for (DataBaseType dataBaseType : DataBaseType.values()) {
				if (lowerCase.contains(dataBaseType.getValue())) {
					return dataBaseType;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return DataBaseType.未知数据库;
	}

	public static WFDataSource getDefaultDataSource() {
		return defaultDataSource;
	}
}
