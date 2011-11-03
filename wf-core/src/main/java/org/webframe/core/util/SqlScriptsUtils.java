/*
 * wf-core
 * Created on 2011-10-31-下午03:37:54
 */

package org.webframe.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SqlScripts工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-10-31 下午03:37:54
 */
public abstract class SqlScriptsUtils {

	private static Log	log				= LogFactory.getLog(SqlScriptsUtils.class);

	public static String	SQL_SEMICOLON	= ";";

	public static String	SQL_NOTE			= "(--)|(\\/\\*)";

	/**
	 * 解析sql脚本文件，去除注释（"--"或"/*"开始的行），返回记录sql语句所在行的Map集合，Map的key为sql语句所在的行。
	 * 
	 * @param input sql脚本输入流
	 * @param encoding sql脚本文件编码
	 * @return
	 * @author 黄国庆 2011-10-31 下午09:12:06
	 */
	public static Map<String, String> analyzeSqlFile(InputStream input, String encoding) {
		try {
			return analyzeSqlFile(new BufferedReader(new InputStreamReader(input, encoding)));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			return new LinkedHashMap<String, String>();
		}
	}

	/**
	 * 解析sql脚本文件，去除注释（"--"或"/*"开始的行），返回记录sql语句所在行的Map集合，Map的key为sql语句所在的行。
	 * 
	 * @param input sql脚本输入流
	 * @return
	 * @author 黄国庆 2011-10-31 下午09:17:01
	 */
	public static Map<String, String> analyzeSqlFile(InputStream input) {
		return analyzeSqlFile(new BufferedReader(new InputStreamReader(input)));
	}

	/**
	 * 解析SQL语句字符串为sqlMap
	 * 
	 * @param sqlString SQL语句字符串
	 * @return
	 * @author 黄国庆 2011-11-3 上午10:29:24
	 */
	public static Map<String, String> analyzeSqlFile(String sqlString) {
		StringTokenizer tokenizer = new StringTokenizer(sqlString, ";");
		Map<String, String> sqlMap = new LinkedHashMap<String, String>();
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			i++;
			String tokenSql = tokenizer.nextToken();
			if ("".equals(tokenSql.trim())) continue;
			sqlMap.put(i + "-", tokenSql);
		}
		return sqlMap;
	}

	/**
	 * 解析sql脚本文件，去除注释（"--"或"/*"开始的行），返回记录sql语句所在行的Map List集合，Map的key为sql语句所在的行。
	 * 
	 * @param reader
	 * @return
	 * @author 黄国庆 2011-10-31 下午09:13:17
	 */
	public static Map<String, String> analyzeSqlFile(BufferedReader reader) {
		Map<String, String> sqlMap = new LinkedHashMap<String, String>();
		try {
			// 多行sql语句组装字符串
			StringBuilder sb = new StringBuilder();
			// 每行sql语句字符串
			String line;
			// 行数
			int i = 0;
			while ((line = reader.readLine()) != null) {
				i++;
				// 去除sql脚本注释
				String[] notes = line.split(SQL_NOTE, 2);
				if (notes.length == 2 && log.isDebugEnabled()) {
					log.debug("第" + i + "行sql注释内容为：--" + notes[1]);
				}
				line = notes[0];
				// 使用;分割sql脚本语句
				if (line.contains(SQL_SEMICOLON)) {
					String[] sqls = line.split(SQL_SEMICOLON);
					// 单行字符串sql语句数目
					int j = 0;
					for (String sql : sqls) {
						j++;
						if ("".equals(sql)) continue;
						sb.append(sql);
						if (j == sqls.length && !line.endsWith(SQL_SEMICOLON)) {
							sb.append("\n");
							continue;
						}
						if (log.isDebugEnabled()) {
							log.debug("第" + i + "-" + j + "行sql语句为：" + sb.toString() + ";");
						}
						sqlMap.put(i + "-" + j, sb.toString() + ";");
						sb = new StringBuilder();
					}
				} else {
					if (!"".equals(line)) sb.append(line).append("\n");
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return sqlMap;
	}

	/**
	 * 批量处理sql语句。如果数据库不支持批量处理，使用
	 * {@link org.webframe.core.util.SqlScriptsUtils#executeSql(java.util.Map, javax.sql.DataSource)}
	 * 方法
	 * 
	 * @param sqlMap SQL语句map集合
	 * @param ds 数据源
	 * @throws SQLException
	 * @author 黄国庆 2011-10-31 下午09:56:38
	 */
	public static void executeBatchSql(Map<String, String> sqlMap, DataSource ds) throws SQLException {
		Connection conn = ds.getConnection();
		Statement stat = null;
		try {
			log.debug("Execute SQL:");
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			for (Entry<String, String> entry : sqlMap.entrySet()) {
				log.debug(entry.getValue());
				try {
					stat.addBatch(entry.getValue());
				} catch (SQLException e) {
					StringBuilder error = new StringBuilder("Execute SQL ERROR: " + e);
					error.append("\n").append("\nError SQL:\n" + entry.getValue());
					error.append("\n").append("\nError line: (" + entry.getKey() + ")");
					throw new SQLException(error.toString());
				}
			}
			int[] rows = stat.executeBatch();
			conn.commit();
			log.debug("Execute SQL Rows(" + rows + ")");
		} finally {
			if (stat != null) stat.close();
			conn.close();
		}
	}

	/**
	 * 循环执行sqlMap集合中的SQL语句
	 * 
	 * @param sqlMap SQL语句map集合
	 * @param ds 数据源
	 * @throws SQLException
	 * @author 黄国庆 2011-11-3 上午10:11:38
	 */
	public static void executeSql(Map<String, String> sqlMap, DataSource ds) throws SQLException {
		Connection conn = ds.getConnection();
		try {
			log.debug("Execute SQL:");
			for (Entry<String, String> entry : sqlMap.entrySet()) {
				log.debug(entry.getValue());
				Statement stat = null;
				try {
					stat = conn.createStatement();
					stat.execute(entry.getValue());
				} catch (SQLException e) {
					StringBuilder error = new StringBuilder("Execute SQL ERROR: " + e);
					error.append("\n").append("\nError SQL:\n" + entry.getValue());
					error.append("\n").append("\nError line: (" + entry.getKey() + ")");
					throw new SQLException(error.toString());
				} finally {
					if (stat != null) stat.close();
				}
			}
		} finally {
			conn.close();
		}
	}
}
