
package org.webframe.core.datasource;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.webframe.core.util.SqlScriptsUtils;

/**
 * 数据源工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-17
 *          下午02:23:51
 */
public abstract class DataSourceUtil {

	private static DataBaseType	defaultDataBaseType	= DataBaseType.未知数据库;

	private static WFDataSource	dataSource				= null;

	static void initDataBaseType(DataBaseType dataBaseType) {
		defaultDataBaseType = dataBaseType;
	}

	static void initDataSource(WFDataSource ds) {
		dataSource = ds;
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
		if (DataSourceUtil.getDataBaseType() != DataBaseType.HSQLDB) {
			SqlScriptsUtils.executeBatchSql(SqlScriptsUtils.analyzeSqlFile(IOUtils.toString(initScripts)), ds);
		} else {
			SqlScriptsUtils.executeSql(SqlScriptsUtils.analyzeSqlFile(IOUtils.toString(initScripts)), ds);
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
		executeSqlScripts(initScripts, dataSource);
	}

	public static DataBaseType getDataBaseType() {
		if (defaultDataBaseType == null) {
			defaultDataBaseType = DataBaseType.未知数据库;
		}
		return defaultDataBaseType;
	}

	public static WFDataSource getDataSource() {
		return dataSource;
	}
}
