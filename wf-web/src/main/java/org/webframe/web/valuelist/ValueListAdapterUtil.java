
package org.webframe.web.valuelist;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.model.form.ViewElement;
import org.webframe.core.model.form.ViewElement.QueryConditionType;
import org.webframe.core.util.BeanUtils;
import org.webframe.core.util.DataSourceUtils;
import org.webframe.web.page.ValueListAdapter;
import org.webframe.web.page.adapter.ValueListAdapterManager;
import org.webframe.web.spring.ServiceHelper;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-20
 *          下午04:24:48
 */
public abstract class ValueListAdapterUtil {

	private static final Log							log						= LogFactory.getLog(ValueListAdapterUtil.class);

	private static Map<String, ValueListAdapter>	adapterCache			= null;

	private static final String						parentHqlAdapterName	= "abstractHibernate30Adapter";

	private static final String						parentSqlAdapterName	= "sqlSimpleHashMapAdapter";

	static void addValueListAdapterMap(Map<String, ValueListAdapter> adapterMap) {
		if (adapterCache == null) {
			adapterCache = adapterMap;
		} else {
			adapterCache.putAll(adapterMap);
		}
	}

	private static void addValueListAdapter(String adapterName, ValueListAdapter valueListAdapter) {
		if (adapterCache != null) {
			if (!hasAdapter(adapterName)) {
				adapterCache.put(adapterName, valueListAdapter);
			} else {
				throw new IllegalArgumentException("Spring 已注入(" + adapterName + ") Adapter!");
			}
		} else {
			adapterCache = ValueListAdapterManager.getAllAdapters();
		}
	}

	public static boolean hasAdapter(String adapterName) {
		if (adapterCache == null) {
			adapterCache = ValueListAdapterManager.getAllAdapters();
		}
		return adapterCache.containsKey(adapterName);
	}

	public static boolean hasAdapter(Class<? extends BaseEntity> entityClass) {
		return hasAdapter(generateHqlAdapterName(entityClass));
	}

	public static ValueListAdapter createAdapter(String adapterName, String parentAdapterName) {
		ValueListAdapter valueListAdapter = (ValueListAdapter) ServiceHelper.createBean(adapterName, parentHqlAdapterName);
		addValueListAdapter(adapterName, valueListAdapter);
		return valueListAdapter;
	}

	public static ValueListAdapter createHqlAdapter(String adapterName) {
		return createAdapter(adapterName, parentHqlAdapterName);
	}

	public static ValueListAdapter createSqlAdapter(String adapterName) {
		return createAdapter(adapterName, parentSqlAdapterName);
	}

	public static ValueListAdapter generateHqlAdapter(Class<? extends BaseEntity> entityClass, Properties properties) {
		ValueListAdapter valueListAdapter = createHqlAdapter(generateHqlAdapterName(entityClass));
		Properties props = new Properties();
		props.put("hql", generateHqlAdapterDefaultListHql(entityClass));
		if (properties != null) {
			props.putAll(properties);
		}
		BeanUtils.setBeanProperties(valueListAdapter, props);
		return valueListAdapter;
	}

	public static ValueListAdapter generateSqlAdapter(String adapterName, Properties properties) {
		ValueListAdapter valueListAdapter = createSqlAdapter(adapterName);
		BeanUtils.setBeanProperties(valueListAdapter, properties);
		return valueListAdapter;
	}

	public static String generateHqlAdapterName(Class<? extends BaseEntity> entityClass) {
		return entityClass.getSimpleName() + "HqlListAdapter";
	}

	static String generateHqlAdapterDefaultListHql(Class<? extends BaseEntity> entityClass) {
		String entityName = entityClass.getName();
		StringBuilder hql = new StringBuilder("FROM ");
		hql.append(entityName).append("\n");
		hql.append("WHERE 1=1 ").append("\n");
		BaseEntity entity = BeanUtils.instantiateClass(entityClass);
		List<ViewElement> list = entity.viewElementList();
		// 字符串连接符
		String contact = "";
		switch (DataSourceUtils.getDataBaseType()) {
			case MYSQL :
			case ORACLE :
				contact = "||";
				break;
			case SQLSERVER :
			case HSQLDB :
				contact = "+";
				break;
		}
		for (ViewElement viewElement : list) {
			if (!viewElement.isQuery()) continue;
			StringBuilder query = new StringBuilder("/~");
			String property = viewElement.getProperty();
			if (viewElement.getQueryConditionType() == QueryConditionType.区间类型) {
				query.append("begin" + property);
				query.append(": AND ");
				query.append(property);
				query.append(" >= {");
				query.append("begin" + property);
				query.append("} ~/\n/~");
				query.append("end" + property);
				query.append(": AND ");
				query.append(property);
				query.append(" <= {");
				query.append("end" + property);
				query.append("} ");
			} else {
				query.append(property);
				query.append(": AND ");
				query.append(property);
				if (viewElement.getQueryConditionType() == QueryConditionType.布尔类型) {
					query.append(" is {");
					query.append(property);
					query.append("} ");
				} else if (viewElement.getQueryConditionType() == QueryConditionType.等值类型) {
					query.append(" = {");
					query.append(property);
					query.append("} ");
				} else {
					query.append(" like '%'").append(contact).append("{");
					query.append(property);
					query.append("}").append(contact).append("'%' ");
				}
			}
			hql.append(query).append("~/\n");
		}
		if (log.isInfoEnabled()) {
			log.info("AdapterHQL: " + hql.toString());
		}
		return hql.toString();
	}
}
