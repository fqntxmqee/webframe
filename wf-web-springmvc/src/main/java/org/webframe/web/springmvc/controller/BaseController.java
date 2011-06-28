
package org.webframe.web.springmvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.BeanUtils;
import org.webframe.web.exception.WebFrameException;
import org.webframe.web.util.PatternUtil;

/**
 * webframe控制器，并处理Service异常
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:55:22
 */
public class BaseController extends MultiActionController {

	/**
	 * valuelist 查询页面，查询条件form元素的name名称正则， 例如：<input type="text" name="attribute(name)" />; <input
	 * type="hidden" name="attribute(id)" />
	 */
	private static final String	ATTR_MAP_REGEX	= "attribute\\((\\S*)\\)";

	protected Log						log				= LogFactory.getLog(getClass());

	@Override
	protected void bind(HttpServletRequest request, Object command) throws WebFrameException {
		try {
			super.bind(request, command);
		} catch (Exception e) {
			throwWebFrameException(command.toString() + "类型数据绑定错误！", e);
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
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getQueryMap(HttpServletRequest request, Class<? extends BaseEntity> clazz) {
		Map<String, String[]> mapParam = request.getParameterMap();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		for (Map.Entry<String, String[]> entry : mapParam.entrySet()) {
			String key = entry.getKey();
			List<String> mathsList = PatternUtil.matchs(ATTR_MAP_REGEX, key);
			if (!mathsList.isEmpty() && entry.getValue() != null) {
				String name = mathsList.get(0);
				// 如果clazz为null，不验证数据类型，无法转换数据类型
				if (clazz == null) {
					if (entry.getValue().length >= 1) {
						attrMap.put(name, entry.getValue()[0]);
					}
					continue;
				}
				Class<?> propertyClass = BeanUtils.findPropertyType(name, new Class<?>[]{
					clazz});
				for (String value : entry.getValue()) {
					if (value == null || "".equals(value)) continue;
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
	protected Map<String, Object> getQueryMap(HttpServletRequest request) {
		return getQueryMap(request, null);
	}

	/**
	 * spring 提供为业务模型数据绑定，选择特殊的属性编辑器。 例如：binder.registerCustomEditor(Map.class, new
	 * CustomMapEditor(Map.class));
	 * 
	 * @param binder
	 * @param request
	 * @author: 黄国庆 2011-1-22 下午12:04:17
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder, WebRequest request) {
	}

	/**
	 * 抛出webframe框架异常
	 * 
	 * @param msg 消息
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 下午12:08:38
	 */
	protected void throwWebFrameException(String msg) throws WebFrameException {
		throw new WebFrameException(msg);
	}

	/**
	 * 抛出webframe框架异常
	 * 
	 * @param msg 消息
	 * @param cause 异常对象
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 下午12:09:00
	 */
	protected void throwWebFrameException(String msg, Throwable cause) throws WebFrameException {
		throw new WebFrameException(msg, cause);
	}

	/**
	 * 将xml或json字符串以text/xml类型、UTF-8编码写入response 输出流中
	 * 
	 * @param res
	 * @param sb xml 或 json 字符串
	 * @throws IOException
	 * @author 黄国庆 2011-4-25 下午03:45:02
	 */
	protected void doAjax(HttpServletResponse res, StringBuilder sb) throws IOException {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/xml");
		PrintWriter out = res.getWriter();
		out.print(sb.toString());
		out.flush();
	}
}
