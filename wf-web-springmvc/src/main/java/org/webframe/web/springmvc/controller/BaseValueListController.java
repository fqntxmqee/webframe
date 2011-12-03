
package org.webframe.web.springmvc.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.PropertyConfigurerUtils;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListRequestUtil;
import org.webframe.web.page.web.mvc.ValueListHandlerHelper;
import org.webframe.web.spring.WFClassPathXmlApplicationContext;
import org.webframe.web.util.WebFrameUtils;
import org.webframe.web.valuelist.ValueListAdapterUtil;

/**
 * 提供ValueList分页Controller
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:22:38
 */
public class BaseValueListController extends BaseController {

	/**
	 * 默认valuelist 保存到request中的变量名
	 */
	protected static final String		DEFAULT_VALUELIST_REQUEST_LIST_NAME	= "vlhMap";

	/**
	 * Request域中分页显示记录数param key
	 */
	protected static final String		PARAM_PAGING_NUMBER_PER					= "limit";

	/**
	 * Request域中第几页param key
	 */
	protected static final String		PARAM_PAGING_PAGE							= "start";

	protected static final String		PARAM_DEBUG									= "debug";

	protected static final String		BEAN_NAME_VALUELIST_HELPER				= "valueListHelper";

	private ValueListHandlerHelper	valueListHelper;

	/**
	 * 根据业务模块模型对象类型，获取该模块默认列表页的Hql查询语句的valuelist Adapter，包括查询条件；
	 * 业务模块模型对象的getViewElementList()方法提供查询条件;
	 * 
	 * @param entityClass 业务模块模型对象类型
	 * @author: 黄国庆 2011-1-22 下午12:10:10
	 */
	protected void getDefaultListHqlAdapter(Class<? extends BaseEntity> entityClass) {
		/**
		 * 如果valuelist Adapter容器中没有该模型对象的Adapter，则生成该模型对象的Adapter， 并保存到valuelist Adapter容器中
		 */
		if (!ValueListAdapterUtil.hasAdapter(entityClass)) {
			ValueListAdapterUtil.generateHqlAdapter(entityClass, null);
		}
	}

	/**
	 * 将valuelist中的list拼成json，返回json数组，例如：[]或[{},{}]
	 * 
	 * @param list valuelist实例
	 */
	protected StringBuilder valueListToJson(ValueList<?> list) {
		StringBuilder sb = new StringBuilder("[");
		List<?> list_ = list.getList();
		if (list_ != null && list_.size() > 0) {
			Object obj = list_.get(0);
			if (obj instanceof BasicDynaBean) {
				sb.append(basicDynaBeanToJson(list_));
			} else if (obj instanceof BaseEntity) {
				sb.append(baseEntityToJson(list_));
			} else if (obj instanceof HashMap) {
				sb.append(hashMapToJson(list_));
			} else {
				log.warn(obj != null ? "ValueList中集合对象类型为：" + obj.toString() : "null");
			}
		}
		return sb.append("]");
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
	protected void setValueListToRequest(Class<? extends BaseEntity> entityClass, Map<String, Object> queries, HttpServletRequest request) {
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
	protected void setValueListToRequest(Class<? extends BaseEntity> entityClass, HttpServletRequest request) {
		getDefaultListHqlAdapter(entityClass);
		Map<String, Object> queries = getQueryMap(request, entityClass);
		setValueListToRequest(ValueListAdapterUtil.generateHqlAdapterName(entityClass), queries, "listTable", request,
			null);
	}

	/**
	 * 根据valuelist Adapter 名称、valuelist查询条件、valuelist 列表页面vlh标签的id名称、 valuelist
	 * 列表页面vlh标签的id名称，执行业务模块的查询，并把查询结果保存到request域中
	 * 
	 * @param adapter valuelist Adapter 名称
	 * @param queries valuelist 查询条件
	 * @param tableId valuelist 列表页面vlh标签的id名称； 例如：<@vlh.root url="?" value="vlhMap"
	 *           includeParameters="*" id="listTable" configName="microsoftLook">
	 * @param request
	 * @param valueListName 如果没有指定该名称，默认使用key：DEFAULT_VALUELIST_REQUEST_LIST_NAME
	 * @author: 黄国庆 2011-1-22 下午12:17:12
	 */
	protected void setValueListToRequest(String adapter, Map<String, Object> queries, String tableId, HttpServletRequest request, String valueListName) {
		Map<String, Object> queryMap = getValueListQureyMap(queries, tableId, request);
		disposeValueListDebugParam(queries, request);
		ValueListInfo info = getValueListInfo(queryMap);
		ValueList<?> valueList = getValueList(adapter, info);
		if (valueListName == null) valueListName = DEFAULT_VALUELIST_REQUEST_LIST_NAME;
		getValueListHelper().backupAndSet(request, valueList, valueListName, tableId);
	}

	/**
	 * 根据valuelist Adapter 名称、valuelist查询条件、valuelist 列表页面vlh标签的id名称、 valuelist
	 * 列表页面vlh标签的id名称，执行业务模块的查询，并把查询结果保存到request域中
	 * 
	 * @param adapter valuelist Adapter 名称
	 * @param tableId valuelist 列表页面vlh标签的id名称
	 * @param request
	 * @param valueListName 放在request域中的变量名称，如果没有指定该名称，默认使用key：DEFAULT_VALUELIST_REQUEST_LIST_NAME
	 * @author 黄国庆 2011-4-25 下午08:28:39
	 */
	protected void setValueListToRequest(String adapter, String tableId, HttpServletRequest request, String valueListName) {
		setValueListToRequest(adapter, getQueryMap(request), tableId, request, valueListName);
	}

	/**
	 * 根据request域中param，获取valuelist QureyMap，并生成ValueListInfo实例，
	 * 设置ValueListInfo实例属性，查询数据库获取ValueList实例
	 * 
	 * @param adapter valuelist Adapter 名称
	 * @param request
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:38:10
	 */
	protected ValueList<?> getValueList(String adapter, HttpServletRequest request) {
		Map<String, Object> queryMap = getQueryMap(request);
		disposeValueListDebugParam(queryMap, request);
		ValueListInfo info = getValueListInfo(queryMap);
		parsePagingPage(request, info);
		return getValueList(adapter, info);
	}

	/**
	 * 处理分页查询，每页查询的记录条数，指定查询第几页
	 * 
	 * @param request
	 * @param info
	 * @author 黄国庆 2011-12-3 下午05:00:29
	 */
	protected void parsePagingPage(HttpServletRequest request, ValueListInfo info) {
		try {
			// 处理分页查询，每页查询的记录条数
			String pagingNumberPer = request.getParameter(PARAM_PAGING_NUMBER_PER);
			info.setPagingNumberPer(Integer.parseInt(pagingNumberPer));
		} catch (NumberFormatException re) {
			if (!"null".equals(re.getMessage())) {
				log.error("Request Param: " + PARAM_PAGING_NUMBER_PER + "不是数值类型！");
			}
		}
		try {
			// 指定查询第几页
			String pagingPage = request.getParameter(PARAM_PAGING_PAGE);
			info.setPagingPage(Integer.parseInt(pagingPage) / info.getPagingNumberPer() + 1);
		} catch (NumberFormatException re) {
			info.setPagingPage(1); // 不分页时默认按照从第1页开始
			if (!"null".equals(re.getMessage())) {
				log.error("Request Param: " + PARAM_PAGING_NUMBER_PER + "不是数值类型！");
			}
		}
	}

	/**
	 * 获取valueList实例
	 * 
	 * @param adapter valuelist spring配置文件中的Adapter字符串
	 * @param queries 查询条件
	 * @return
	 * @author 黄国庆 2011-4-25 下午04:29:27
	 */
	protected ValueList<?> getValueList(String adapter, Map<String, Object> queries) {
		return getValueList(adapter, getValueListInfo(queries));
	}

	/**
	 * 获取valueList实例
	 * 
	 * @param adapter valuelist spring配置文件中的Adapter字符串
	 * @param info ValueListInfo实例对象
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:10:17
	 */
	protected ValueList<?> getValueList(String adapter, ValueListInfo info) {
		if (info.getFilters().containsKey(PARAM_DEBUG)) {
			reloadValueListSpringContext();
		}
		return getValueListHelper().getValueList(adapter, info);
	}

	/**
	 * 根据查询条件获取ValueListInfo实例对象
	 * 
	 * @param queries 查询条件
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:10:59
	 */
	protected ValueListInfo getValueListInfo(Map<String, Object> queries) {
		return new ValueListInfo(queries);
	}

	/**
	 * 重新加载资源目录valuelist配置文件
	 * 
	 * @author 黄国庆 2011-5-1 下午09:20:25
	 */
	protected void reloadValueListSpringContext() {
		if (PropertyConfigurerUtils.getBoolean("vlh.debug")) {
			log.info("重新加载资源目录valuelist配置文件!");
			try {
				ClassPathXmlApplicationContext cpxac = new WFClassPathXmlApplicationContext(new String[]{
					PropertyConfigurerUtils.getString("vlh.debug.location")}, WebFrameUtils.getApplicationContext());
				ValueListHandlerHelper valueListHelper = cpxac.getBean(BEAN_NAME_VALUELIST_HELPER,
					ValueListHandlerHelper.class);
				setValueListHelper(valueListHelper);
			} catch (BeansException be) {
				be.printStackTrace();
				log.error("重新加载valuelist配置文件失败！");
			}
		}
	}

	protected ValueListHandlerHelper getValueListHelper() {
		return valueListHelper;
	}

	@Autowired
	public void setValueListHelper(ValueListHandlerHelper valueListHelper) {
		this.valueListHelper = valueListHelper;
	}

	/**
	 * 获取valuelist的查询信息
	 * 
	 * @param queries valuelist 查询条件
	 * @param tableId valuelist 列表页面vlh标签的id名称；
	 * @param request
	 * @return
	 * @author: 黄国庆 2011-1-22 下午12:20:29
	 */
	private Map<String, Object> getValueListQureyMap(Map<String, Object> queries, String tableId, HttpServletRequest request) {
		Map<String, Object> queryMap = ValueListRequestUtil.getRequestParameterMap(request, tableId);
		// 将查询条件两边的空格去掉再放进查询条件的Map中
		for (String key : queryMap.keySet()) {
			Object value = queries.get(key);
			if (value != null && value instanceof String) {
				queryMap.put(key, ((String) value).trim());
			}
		}
		queryMap.putAll(queries);
		return queryMap;
	}

	private StringBuilder basicDynaBeanToJson(List<?> basicDynaBeanList) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<?> iterator = basicDynaBeanList.iterator(); iterator.hasNext();) {
			BasicDynaBean dynaBean = (BasicDynaBean) iterator.next();
			DynaProperty[] dynaProperties = dynaBean.getDynaClass().getDynaProperties();
			sb.append("{");
			for (int i = 0; i < dynaProperties.length; i++) {
				sb.append("\"");
				sb.append(dynaProperties[i].getName());
				sb.append("\":");
				Object o = dynaBean.get(dynaProperties[i].getName());
				String value = (o == null) ? "" : o.toString();
				sb.append(JSONUtils.valueToString(value));
				if (i != dynaProperties.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		return sb;
	}

	private StringBuilder baseEntityToJson(List<?> basicDynaBeanList) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<?> iterator = basicDynaBeanList.iterator(); iterator.hasNext();) {
			BaseEntity baseEntity = (BaseEntity) iterator.next();
			String string = JSONObject.fromObject(baseEntity).toString();
			sb.append(string);
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		return sb;
	}

	private StringBuilder hashMapToJson(List<?> basicDynaBeanList) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<?> iterator = basicDynaBeanList.iterator(); iterator.hasNext();) {
			HashMap<?, ?> hashMap = (HashMap<?, ?>) iterator.next();
			String string = JSONObject.fromObject(hashMap).toString();
			sb.append(string);
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		return sb;
	}

	private void disposeValueListDebugParam(Map<String, Object> queries, HttpServletRequest request) {
		if (request.getParameter(PARAM_DEBUG) != null) {
			queries.put(PARAM_DEBUG, true);
		}
	}
}
