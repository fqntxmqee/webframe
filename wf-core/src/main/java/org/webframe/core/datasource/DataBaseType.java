
package org.webframe.core.datasource;

/**
 * 数据库类型枚举
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-29
 *          下午02:14:15
 */
public enum DataBaseType {
	MYSQL("mysql", "mysql"), ORACLE("oracle", "oracle"), SQLSERVER("sqlserver", "sqlserver"), HSQLDB("hsqldb", "hsqldb"), 未知数据库(
				"unknown", "unknown");

	private final String	value;

	private final String	label;

	private DataBaseType(String label, String value) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static DataBaseType getName(String value) {
		for (DataBaseType type : DataBaseType.values()) {
			if (type.getValue().startsWith(value)) {
				return type;
			}
		}
		return DataBaseType.未知数据库;
	}

	public String toString() {
		return this.getValue();
	}
}
