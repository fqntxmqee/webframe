/*
 * wf-web
 * Created on 2011-5-9-下午09:22:09
 */

package org.webframe.web.valuelist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.BeanUtils;
import org.webframe.core.util.PropertyConfigurerUtils;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.ValueListAdapterManager;
import org.webframe.web.page.web.ValueListRequestUtil;
import org.webframe.web.page.web.mvc.ValueListHandlerHelper;
import org.webframe.web.spring.WFClassPathXmlApplicationContext;
import org.webframe.web.util.PatternUtil;
import org.webframe.web.util.WebFrameUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-9 下午09:22:09
 */
public class ValueListUtils {

	/**
	 * valuelist 查询页面，查询条件form元素的name名称正则， 例如：<p>&ltinput type="text" name="attribute(name)" />;
	 * <p>&ltinput type="hidden" name="attribute(id)" />
	 */
	public final static String					ATTR_MAP_REGEX								= "attribute\\((\\S*)\\)";

	public final static String					BEAN_NAME_VALUELIST_HELPER				= "valueListHelper";

	/**
	 * 默认valuelist 保存到request中的变量名
	 */
	public final static String					DEFAULT_VALUELIST_REQUEST_LIST_NAME	= "vlhMap";

	/**
	 * Request域中分页显示记录数param key
	 */
	public final static String					PARAM_PAGING_NUMBER_PER					= "limit";

	/**
	 * Request域中第几页param key
	 */
	public final static String					PARAM_PAGING_PAGE							= "start";

	public final static String					PARAM_DEBUG									= "debug";

	private final static Log					log											= LogFactory.getLog(ValueListUtils.class);

	private static ValueListHandlerHelper	valueListHelper;

	/**
	 * 根据业务模块模型对象类型，获取该模块默认列表页的Hql查询语句的valuelist Adapter，包括查询条件；
	 * 业务模块模型对象的getViewElementList()方法提供查询条件;
	 * 
	 * @param entityClass 业务模块模型对象类型
	 * @author: 黄国庆 2011-1-22 下午12:10:10
	 */
	public static void getDefaultListHqlAdapter(Class<? extends BaseEntity> entityClass) {
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
	public static JSONArray valueListToJson(ValueList<?> list) {
		List<?> list_ = list.getList();
		if (list_ != null) {
			return JSONArray.fromObject(list_);
		}
		return new JSONArray();
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
	public static void setValueListToRequest(Class<? extends BaseEntity> entityClass, Map<String, Object> queries, HttpServletRequest request) {
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
	public static void setValueListToRequest(Class<? extends BaseEntity> entityClass, HttpServletRequest request) {
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
	public static void setValueListToRequest(String adapter, Map<String, Object> queries, String tableId, HttpServletRequest request, String valueListName) {
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
	public static void setValueListToRequest(String adapter, String tableId, HttpServletRequest request, String valueListName) {
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
	public static <X> ValueList<X> getValueList(String adapter, HttpServletRequest request) {
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
	public static void parsePagingPage(HttpServletRequest request, ValueListInfo info) {
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
	public static <X> ValueList<X> getValueList(String adapter, Map<String, Object> queries) {
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
	public static <X> ValueList<X> getValueList(String adapter, ValueListInfo info) {
		return getValueListHelper().getValueList(adapter, info);
	}

	/**
	 * 根据查询条件获取ValueListInfo实例对象
	 * 
	 * @param queries 查询条件
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:10:59
	 */
	public static ValueListInfo getValueListInfo(Map<String, Object> queries) {
		return new ValueListInfo(queries);
	}

	/**
	 * 重新加载资源目录valuelist配置文件
	 * 
	 * @author 黄国庆 2011-5-1 下午09:20:25
	 */
	public static void reloadValueListSpringContext() {
		if (PropertyConfigurerUtils.getBoolean("vlh.debug")) {
			log.info("重新加载资源目录valuelist配置文件!");
			try {
				WFClassPathXmlApplicationContext wfcax = new WFClassPathXmlApplicationContext(new String[]{
					PropertyConfigurerUtils.getString("vlh.debug.location")}, false, WebFrameUtils.getApplicationContext());
				wfcax.setAllowBeanDefinitionOverriding(true);
				wfcax.refresh();
				Map<String, ValueListAdapterManager> maps = wfcax.getBeansOfType(ValueListAdapterManager.class);
				for (String key : maps.keySet()) {
					ValueListAdapterManager adapterManager = maps.get(key);
					adapterManager.postProcessBeforeInitialization(wfcax.getBean("valueListHandler"), "valueListHandler");
				}
			} catch (BeansException be) {
				log.error("重新加载valuelist配置文件失败！", be);
			}
		}
	}

	/**
	 * 从request域中获取查询条件，页面form表单元素的name符合ATTR_MAP_REGEX正则的所有集合， 如果没有，则返回空的Map，不返回null
	 * 
	 * @param request
	 * @param clazz Hql语句，根据业务模型属性类型验证查询条件，并转换数据类型
	 * @return
	 * @author: 黄国庆 2011-1-22 下午12:06:38
	 */
	public static Map<String, Object> getQueryMap(HttpServletRequest request, Class<? extends BaseEntity> clazz) {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Set<?> keys = request.getParameterMap().keySet();
		for (Object key : keys) {
			if (key == null) continue;
			String value = request.getParameter(key.toString());
			if (value != null) {
				List<String> mathsList = PatternUtil.matchs("attribute\\((\\S*)\\)", key.toString());
				if (!mathsList.isEmpty()) {
					String name = mathsList.get(0);
					// 如果clazz为null，不验证数据类型，无法转换数据类型
					if (clazz == null) {
						attrMap.put(name, value);
						continue;
					}
					Class<?> propertyClass = BeanUtils.findPropertyType(name, new Class<?>[]{
						clazz});
					// 如果查询条件属性对应的model属性的类型为Boolean或boolean，将查询条件的值转换为boolean类型
					if (Boolean.class.isAssignableFrom(propertyClass) || boolean.class.equals(propertyClass)) {
						attrMap.put(name, Boolean.parseBoolean(value));
					} else if (Integer.class.isAssignableFrom(propertyClass) || int.class.equals(propertyClass)) {
						attrMap.put(name, Integer.parseInt(value));
					} else if (Double.class.isAssignableFrom(propertyClass) || double.class.equals(propertyClass)) {
						attrMap.put(name, Double.parseDouble(value));
					} else if (Float.class.isAssignableFrom(propertyClass) || float.class.equals(propertyClass)) {
						attrMap.put(name, Float.parseFloat(value));
					} else {
						attrMap.put(name, value);
					}
				} else {
					attrMap.put(key.toString(), value);
				}
			}
		}
		return attrMap;
	}

	/**
	 * 从request域中获取查询条件，页面form表单元素的name符合ATTR_MAP_REGEX正则的所有集合， 如果没有，则返回空的Map，不返回null
	 * 
	 * @param request
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:19:10
	 */
	public static Map<String, Object> getQueryMap(HttpServletRequest request) {
		return getQueryMap(request, null);
	}

	public static ValueListHandlerHelper getValueListHelper() {
		if (valueListHelper == null) {
			valueListHelper = (ValueListHandlerHelper) WebFrameUtils.getBean(BEAN_NAME_VALUELIST_HELPER);
		}
		return valueListHelper;
	}

	public static void setValueListHelper(ValueListHandlerHelper helper) {
		valueListHelper = helper;
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
	private static Map<String, Object> getValueListQureyMap(Map<String, Object> queries, String tableId, HttpServletRequest request) {
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

	private static void disposeValueListDebugParam(Map<String, Object> queries, HttpServletRequest request) {
		if (request.getParameter(PARAM_DEBUG) != null) {
			queries.put(PARAM_DEBUG, true);
		}
	}
}
