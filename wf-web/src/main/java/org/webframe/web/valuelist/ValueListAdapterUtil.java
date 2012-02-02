
package org.webframe.web.valuelist;

import java.util.Map;
import java.util.Properties;

import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.BeanUtils;
import org.webframe.web.page.ValueListAdapter;
import org.webframe.web.page.adapter.ValueListAdapterManager;
import org.webframe.web.spring.ServiceHelper;

/**
 * ValueListApapter工具类，提供动态创建Adapter
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:32:00
 * @version
 */
public abstract class ValueListAdapterUtil {

	private static Map<String, ValueListAdapter>	adapterCache			= null;

	// 确任与wf-web-page模块中的wf-page-hql.xml配置文件中的Adapter名字一致
	private static final String						parentHqlAdapterName	= "abstractHibernateAdapter";

	// 确任与wf-web-page模块中的wf-page-sql.xml配置文件中的Adapter名字一致
	private static final String						parentSqlAdapterName	= "sqlHashMapAdapter";

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

	public static ValueListAdapter generateSqlAdapter(String adapterName, Properties properties) {
		ValueListAdapter valueListAdapter = createSqlAdapter(adapterName);
		BeanUtils.setBeanProperties(valueListAdapter, properties);
		return valueListAdapter;
	}

	public static String generateHqlAdapterName(Class<? extends BaseEntity> entityClass) {
		return entityClass.getSimpleName() + "HqlListAdapter";
	}
}
