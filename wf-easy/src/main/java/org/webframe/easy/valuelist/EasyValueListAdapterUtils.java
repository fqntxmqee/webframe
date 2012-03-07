/*
 * wf-easy
 * Created on 2012-2-2-上午09:57:30
 */

package org.webframe.easy.valuelist;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.core.datasource.DataSourceUtil;
import org.webframe.core.util.BeanUtils;
import org.webframe.easy.model.EasyEntity;
import org.webframe.easy.model.form.ViewElement;
import org.webframe.easy.model.form.ViewElement.QueryConditionType;
import org.webframe.web.page.ValueListAdapter;
import org.webframe.web.valuelist.ValueListAdapterUtil;

/**
 * 简单开发模块ValueListAdapter工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-2 上午09:57:30
 * @version
 */
public class EasyValueListAdapterUtils extends ValueListAdapterUtil {

	private static final Log	log	= LogFactory.getLog(EasyValueListAdapterUtils.class);

	public static ValueListAdapter generateHqlAdapter(Class<? extends EasyEntity> entityClass, Properties properties) {
		ValueListAdapter valueListAdapter = createHqlAdapter(generateHqlAdapterName(entityClass));
		Properties props = new Properties();
		props.put("hql", generateHqlAdapterDefaultListHql(entityClass));
		if (properties != null) {
			props.putAll(properties);
		}
		BeanUtils.setBeanProperties(valueListAdapter, props);
		return valueListAdapter;
	}

	static String generateHqlAdapterDefaultListHql(Class<? extends EasyEntity> entityClass) {
		String entityName = entityClass.getName();
		StringBuilder hql = new StringBuilder("FROM ");
		hql.append(entityName).append("\n");
		hql.append("WHERE 1=1 ").append("\n");
		EasyEntity entity = BeanUtils.instantiateClass(entityClass);
		List<ViewElement> list = entity.viewElementList();
		// 字符串连接符
		String contact = "";
		switch (DataSourceUtil.getDataBaseType()) {
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
