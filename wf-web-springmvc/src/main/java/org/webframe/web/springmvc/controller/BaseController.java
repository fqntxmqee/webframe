
package org.webframe.web.springmvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.BeanUtils;
import org.webframe.web.page.exp.ValueListException;
import org.webframe.web.springmvc.bean.AjaxError;
import org.webframe.web.springmvc.exp.AjaxException;
import org.webframe.web.util.PatternUtil;

/**
 * webframe控制器，并处理Service异常
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:55:22
 */
public class BaseController {

	/**
	 * valuelist 查询页面，查询条件form元素的name名称正则， 例如：&ltinput type="text" name="attribute(name)" />;
	 * &ltinput type="hidden" name="attribute(id)" />
	 */
	protected static final String	ATTR_MAP_REGEX			= "attribute\\((\\S*)\\)";

	protected Log						log						= LogFactory.getLog(getClass());

	/**
	 * 存放在Request attribut中的ajax变量；如果存在，表示为ajax请求
	 */
	protected static final String	ATTR_IS_AJAX			= "isAjax";

	/**
	 * 存放在Request attribut中的返回页面，用于业务异常
	 */
	protected static final String	ATTR_RETURN_PAGE		= "returnPage";

	/**
	 * 默认异常错误页面
	 */
	protected static final String	PAGE_DEFAULT_ERROR	= "/exp/error";

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex, HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setStatus(500);
		ModelAndView modelAndView = null;
		if (ex instanceof AjaxException) {
			return handleAjaxException((AjaxException) ex, res);
		} else if (ex instanceof ServiceException) {
			modelAndView = handleServiceException((ServiceException) ex, req, res);
		}
		if (modelAndView == null) {
			modelAndView = getErrorView(req);
		}
		modelAndView.getModel().put("exception", getMessage(ex));
		modelAndView.getModel().put("url", req.getRequestURL().toString());
		return modelAndView;
	}

	/**
	 * 处理Controller方法中抛出的Service异常
	 * 
	 * @param se Service异常
	 * @param req
	 * @return
	 * @author 黄国庆 2012-1-29 下午10:04:03
	 */
	protected ModelAndView handleServiceException(ServiceException se, HttpServletRequest req, HttpServletResponse res) {
		if (isAjaxRequest(req)) {
			return handleAjaxException(new AjaxException(se), res);
		}
		return getErrorView(req);
	}

	/**
	 * 处理Controller方法中抛出的Ajax异常
	 * 
	 * @param ae Ajax异常
	 * @param res
	 * @return
	 * @author 黄国庆 2012-1-29 下午10:04:35
	 */
	protected ModelAndView handleAjaxException(AjaxException ae, HttpServletResponse res) {
		AjaxError ajaxError = ae.getAjaxError();
		ajaxError.addDetailMsg(getMessage(ae));
		outWriteJSON(res, ajaxError.toString());
		return null;
	}

	/**
	 * 从request域中获取查询条件，页面form表单元素的name符合ATTR_MAP_REGEX正则的所有集合， 如果没有，则返回空的Map，不返回null
	 * 
	 * @param req
	 * @param clazz Hql语句，根据业务模型属性类型验证查询条件，并转换数据类型
	 * @return
	 * @author: 黄国庆 2011-1-22 下午12:06:38
	 */
	protected Map<String, Object> getQueryMap(HttpServletRequest req, Class<? extends BaseEntity> clazz) {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Set<?> keys = req.getParameterMap().keySet();
		for (Object key : keys) {
			if (key == null) continue;
			String value = req.getParameter(key.toString());
			if (value != null) {
				List<String> mathsList = PatternUtil.matchs(ATTR_MAP_REGEX, key.toString());
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
	 * @param req
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:19:10
	 */
	protected Map<String, Object> getQueryMap(HttpServletRequest req) {
		return getQueryMap(req, null);
	}

	/**
	 * 抛出Ajax异常
	 * 
	 * @param msg 消息
	 * @throws AjaxException
	 * @author: 黄国庆 2011-1-22 下午12:08:38
	 */
	protected void throwAjaxException(String msg) throws AjaxException {
		throw new AjaxException(msg);
	}

	/**
	 * 抛出valuelist异常
	 * 
	 * @param adapter valuelist Adapter 名称
	 * @param cause 异常对象
	 * @throws ValueListException
	 * @author: 黄国庆 2011-1-22 下午12:09:00
	 */
	protected void throwValueListException(String adapter, Throwable cause) throws ValueListException {
		throw new ValueListException(adapter, cause);
	}

	/**
	 * 输出文本字符串
	 * 
	 * @param res
	 * @param msg
	 * @author 黄国庆 2012-1-30 上午09:03:46
	 */
	protected void outWrite(HttpServletResponse res, String msg) {
		outWrite(res, msg, "text/xml");
	}

	/**
	 * 输出xml
	 * 
	 * @param res
	 * @param xml
	 * @author 黄国庆 2012-1-30 上午09:03:32
	 */
	protected void outWriteXml(HttpServletResponse res, String xml) {
		outWrite(res, xml, "application/xml");
	}

	/**
	 * 输出JSON
	 * 
	 * @param res
	 * @param json
	 * @author 黄国庆 2012-1-30 上午09:03:20
	 */
	protected void outWriteJSON(HttpServletResponse res, String json) {
		outWrite(res, json, "application/json");
	}

	/**
	 * 获取异常信息字符串，并记录日志
	 * 
	 * @param throwable
	 * @return
	 * @author 黄国庆 2011-12-4 上午11:43:21
	 */
	protected String getMessage(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		log.error(sw);
		return sw.toString();
	}

	/**
	 * 设置为Ajax请求
	 * 
	 * @param req
	 * @author 黄国庆 2012-1-30 上午09:06:15
	 */
	protected final void setAjaxRequest(HttpServletRequest req) {
		req.setAttribute(ATTR_IS_AJAX, true);
	}

	/**
	 * 是否是Ajax请求
	 * 
	 * @param req
	 * @author 黄国庆 2012-1-30 上午09:13:35
	 */
	protected final boolean isAjaxRequest(HttpServletRequest req) {
		return req.getAttribute(ATTR_IS_AJAX) != null;
	}

	/**
	 * 非Ajax请求方法异常跳转页面
	 * 
	 * @param req
	 * @param errorPage
	 * @author 黄国庆 2012-1-30 下午02:04:14
	 */
	protected final void setErrorPage(HttpServletRequest req, String errorPage) {
		req.setAttribute(ATTR_RETURN_PAGE, errorPage);
	}

	/**
	 * 获取错误view，如果request域中设置了{@link ATTR_RETURN_PAGE}属性，则返回指定的错误view；否则返回默认的
	 * {@link PAGE_DEFAULT_ERROR}
	 * 
	 * @param req
	 * @return
	 * @author 黄国庆 2012-1-30 上午09:26:40
	 */
	protected ModelAndView getErrorView(HttpServletRequest req) {
		ModelAndView modelAndView = new ModelAndView();
		// 如果为业务异常，且指定了业务异常返回页面
		if (req.getAttribute(ATTR_RETURN_PAGE) != null) {
			modelAndView.setViewName(req.getAttribute(ATTR_RETURN_PAGE).toString());
		} else {
			modelAndView.setViewName(PAGE_DEFAULT_ERROR);
		}
		return modelAndView;
	}

	/**
	 * 返回流写入信息，并可以指定内容类型
	 * 
	 * @param res 返回流
	 * @param msg 内容
	 * @param contentType 内容类型
	 * @author 黄国庆 2012-2-16 上午08:06:29
	 */
	protected void outWrite(HttpServletResponse res, String msg, String contentType) {
		res.setContentType(contentType);
		try {
			PrintWriter out = res.getWriter();
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			throw new AjaxException(e);
		}
	}
}
