/*
 * wf-core-jta
 * Created on 2011-6-29-下午04:23:27
 */

package org.webframe.core.jta.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.webframe.core.dao.BaseDao;
import org.webframe.core.datasource.DataBaseType;
import org.webframe.core.jta.ColumnType;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午04:23:27
 */
@Repository
public class JtaDao extends BaseDao implements IJtaDao {

	// 数据库类型
	private DataBaseType						jtaDatabaseType;

	// 表示表不同字段类型的map
	private final Map<String, String>	types	= new HashMap<String, String>();

	@Autowired
	private DataSource						jtaDataSource;

	@Autowired
	public void setJtaDataSource(DataSource jtaDataSource) {
		this.jtaDataSource = jtaDataSource;
		initTypes(getConnection());
	}

	@Override
	public DataBaseType getJtaDataBaseType() {
		return jtaDatabaseType;
	}

	@Override
	public void createTable(String schema, String tableName, String tableContent) {
		Connection conn = getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			tableContent = tableContent.replaceAll("文本", types.get(ColumnType.文本))
				.replaceAll("数字", types.get(ColumnType.数字))
				.replaceAll("字符", types.get(ColumnType.字符))
				.replaceAll("日期", types.get(ColumnType.日期));
			String createTableSql = " create table " + tableName + "(" + tableContent + ",PRIMARY KEY (ID_))";
			stmt.executeUpdate(createTableSql);
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnStmtRs(conn, stmt, null);
		}
	}

	@Override
	public List<String> getAllTables() {
		List<String> list = new ArrayList<String>();
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			DatabaseMetaData info = conn.getMetaData();
			switch (jtaDatabaseType) {
				case MYSQL :
					rs = info.getTables(null, null, null, new String[]{
						"TABLE"});
					break;
				case ORACLE :
					rs = info.getTables(null, info.getUserName(), "%", new String[]{
								"TABLE", "VIEW"});
					break;
				default :
					rs = info.getTables(null, null, null, new String[]{
								"TABLE", "VIEW"});
					break;
			}
			String tmp;
			while (rs.next()) {
				tmp = rs.getString("TABLE_NAME");
				if (tmp.indexOf("$") == -1) list.add(rs.getString(3));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnStmtRs(conn, null, rs);
		}
		return list;
	}

	protected Connection getConnection() {
		return DataSourceUtils.getConnection(jtaDataSource);
	}

	protected DataBaseType getDataBaseType(Connection conn) {
		DataBaseType type = DataBaseType.未知数据库;
		try {
			DatabaseMetaData info = conn.getMetaData();
			String databaseProductName = info.getDatabaseProductName();
			String lowerCase = databaseProductName.replaceAll(" ", "").toLowerCase();
			for (DataBaseType dataBaseType : DataBaseType.values()) {
				if (lowerCase.contains(dataBaseType.getValue())) {
					return dataBaseType;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
	}

	protected void closeConnStmtRs(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void initTypes(Connection conn) {
		jtaDatabaseType = getDataBaseType(conn);
		switch (jtaDatabaseType) {
			case MYSQL :
				types.put(ColumnType.数字.name(), "double");
				types.put(ColumnType.文本.name(), "longtext");
				types.put(ColumnType.日期.name(), "date");
				types.put(ColumnType.字符.name(), "varchar");
				break;
			case ORACLE :
				types.put(ColumnType.数字.name(), "number");
				types.put(ColumnType.文本.name(), "clob");
				types.put(ColumnType.日期.name(), "date");
				types.put(ColumnType.字符.name(), "varchar2");
				break;
			case SQLSERVER :
				types.put(ColumnType.数字.name(), "numeric");
				types.put(ColumnType.文本.name(), "text");
				types.put(ColumnType.日期.name(), "datetime");
				types.put(ColumnType.字符.name(), "nvarchar");
				break;
		}
	}
}
