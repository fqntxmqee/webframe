
package org.webframe.web.page.adapter.map;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.core.datasource.DataBaseType;
import org.webframe.core.datasource.WFDataSource;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.jdbc.AbstractDynaJdbcAdapter;

/**
 * 使用HashMap保存ResultSet中的每一行记录，其中key是该列的列名，value是值。<br/> <b>需要JDBC驱动实现支持ResultSetMetaData。</b><br/>
 * 值得注意的是Oracle JDBC驱动返回的列名貌似都是大写，或者和驱动参数有关？没试。<br/>
 * 谨献给和我一样讨厌什么DynaBean和只能用Struts标签库输出DynaBean结果集的大人们！
 * 
 * @version $Id: SimpleHashMapAdapter.java,v 1.1.2.1 2010/07/05 02:21:21 huangguoqing Exp $
 */
public class HashMapAdapter extends AbstractDynaJdbcAdapter {

	protected Log						log	= LogFactory.getLog(getClass());

	private Map<String, String>	sqlMap;

	/**
	 * @function:
	 * @param sqls
	 * @author: zhaoxiaoqiang 2010-4-23 下午07:37:11
	 */
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	/**
	 * @see org.webframe.web.page.adapter.jdbc.AbstractJdbcAdapter#processResultSet(java.lang.String,
	 *      java.sql.ResultSet, int, org.webframe.web.page.ValueListInfo)
	 */
	@Override
	public List<Map<String, Object>> processResultSet(String name, ResultSet result, int numberPerPage, ValueListInfo info)
				throws SQLException {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(numberPerPage);
		ResultSetMetaData meta = result.getMetaData(); // 获取结果集meta
		Map<String, Object> m;
		int columnCount = meta.getColumnCount();
		for (int i = 0; i < numberPerPage && result.next(); i++) {
			m = new HashMap<String, Object>(columnCount);
			for (int j = 1; j <= columnCount; j++) {
				// key为列名，value为实际数据
				m.put(meta.getColumnLabel(j), result.getObject(j));
			}
			ret.add(m);
		}
		return ret;
	}

	/**
	 * 根据数据库类型，选择不同的sql语句
	 */
	public <X> ValueList<X> getValueList(String name, ValueListInfo info) {
		if (sqlMap != null && sqlMap.size() > 0) {
			DataSource dataSource = getDataSource();
			if (dataSource != null && dataSource instanceof WFDataSource) {
				DataBaseType databaseType = ((WFDataSource) dataSource).getDatabaseType();
				String sql = sqlMap.get(databaseType.getValue());
				if (sql == null) {
					throw new IllegalArgumentException("ValueListAdapter："
								+ name
								+ "不存在"
								+ databaseType.getValue()
								+ "数据库类型sql！");
				} else {
					if (log.isDebugEnabled()) {
						log.debug("ValueListAdapter：" + name + " sql语句：\n" + sql);
					}
					super.setSql(sql);
				}
			}
		}
		return super.getValueList(name, info);
	}
}
