/*
 * wf-easy
 * Created on 2012-2-2-上午10:00:31
 */

package org.webframe.easy.valuelist;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.webframe.easy.model.EasyEntity;
import org.webframe.web.valuelist.ValueListAdapterUtil;
import org.webframe.web.valuelist.ValueListUtils;

/**
 * 简单开发模块ValueList工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-2 上午10:00:31
 * @version
 */
public class EasyValueListUtils extends ValueListUtils {

	/**
	 * 根据业务模块模型对象类型，获取该模块默认列表页的Hql查询语句的valuelist Adapter，包括查询条件；
	 * 业务模块模型对象的getViewElementList()方法提供查询条件;
	 * 
	 * @param entityClass 业务模块模型对象类型
	 * @author: 黄国庆 2011-1-22 下午12:10:10
	 */
	public static void getDefaultListHqlAdapter(Class<? extends EasyEntity> entityClass) {
		/**
		 * 如果valuelist Adapter容器中没有该模型对象的Adapter，则生成该模型对象的Adapter， 并保存到valuelist Adapter容器中
		 */
		if (!EasyValueListAdapterUtils.hasAdapter(entityClass)) {
			EasyValueListAdapterUtils.generateHqlAdapter(entityClass, null);
		}
	}

	/**
	 * 根据业务模块模型对象类型和valuelist查询条件，执行业务模块的查询，并把查询结果保存到request域中，
	 * 默认使用key：DEFAULT_VALUELIST_REQUEST_LIST_NAME
	 * 
	 * @param entityClass 业务模块模型对象类型
	 * @param queries valuelist 查询条件
	 * @param request
	 * @author: 黄国庆 2011-1-22 下午12:09:59
	 */
	public static void setValueListToRequest(Class<? extends EasyEntity> entityClass, Map<String, Object> queries, HttpServletRequest request) {
		getDefaultListHqlAdapter(entityClass);
		setValueListToRequest(ValueListAdapterUtil.generateHqlAdapterName(entityClass), queries, "listTable", request,
			null);
	}

	/**
	 * 根据业务模块模型对象类型从request域中获取valuelist查询条件，然后执行业务模块的查询，并把查询结果保存到request域中，
	 * 默认使用key：DEFAULT_VALUELIST_REQUEST_LIST_NAME，值为：vlhMap
	 * 
	 * @param entityClass 业务模块模型对象类型
	 * @param request
	 * @author: 黄国庆 2011-1-22 下午12:09:59
	 */
	public static void setValueListToRequest(Class<? extends EasyEntity> entityClass, HttpServletRequest request) {
		getDefaultListHqlAdapter(entityClass);
		Map<String, Object> queries = getQueryMap(request, entityClass);
		setValueListToRequest(ValueListAdapterUtil.generateHqlAdapterName(entityClass), queries, "listTable", request,
			null);
	}
}
