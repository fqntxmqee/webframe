
package org.webframe.core.datasource;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据源工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-17
 *          下午02:23:51
 */
public abstract class DataSourceUtil {

	private static final Log		log						= LogFactory.getLog(DataSourceUtil.class);

	private static DataBaseType	defaultDataBaseType	= DataBaseType.未知数据库;

	static void initDataBaseType(DataBaseType dataBaseType) {
		defaultDataBaseType = dataBaseType;
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
		Connection conn = ds.getConnection();
		try {
			String sql = IOUtils.toString(initScripts);
			StringTokenizer tokenizer = new StringTokenizer(sql, ";");
			log.info("Execute DB DataSource with sql:");
			while (tokenizer.hasMoreTokens()) {
				String tokenSql = tokenizer.nextToken();
				if ("".equals(tokenSql.trim())) {
					continue;
				}
				log.info(tokenSql);
				try {
					Statement stat = conn.createStatement();
					stat.execute(tokenSql);
					stat.close();
				} catch (SQLException e) {
					throw new SQLException("execute sql error:" + e + " error sql:\n" + tokenSql + " cause:" + e);
				}
			}
		} finally {
			conn.close();
		}
	}

	public static DataBaseType getDataBaseType() {
		if (defaultDataBaseType == null) {
			defaultDataBaseType = DataBaseType.未知数据库;
		}
		return defaultDataBaseType;
	}
}
