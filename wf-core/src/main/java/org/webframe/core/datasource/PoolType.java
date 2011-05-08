
package org.webframe.core.datasource;

/**
 * 数据库连接池枚举类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-29
 *          下午02:14:15
 */
public enum PoolType {
	DBCP("dbcp", "dbcp"), C3P0("c3p0", "c3p0"), 未知连接池("unknown", "unknown");

	private final String	value;

	private final String	label;

	private PoolType(String label, String value) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static PoolType getName(String value) {
		for (PoolType type : PoolType.values()) {
			if (type.getValue().startsWith(value)) {
				return type;
			}
		}
		return PoolType.未知连接池;
	}

	public String toString() {
		return this.getValue();
	}
}
