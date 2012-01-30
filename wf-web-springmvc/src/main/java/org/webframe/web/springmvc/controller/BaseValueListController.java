
package org.webframe.web.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.exp.ValueListException;
import org.webframe.web.springmvc.exp.AjaxException;
import org.webframe.web.valuelist.ValueListUtils;

/**
 * 提供ValueList分页Controller
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 下午10:09:00
 * @version
 */
public class BaseValueListController extends BaseController {

	/**
	 * 存放在Request param中的debug变量；如果存在，表示为调试，支持动态加载valuelist配置文件
	 */
	protected static final String	PARAM_DEBUG		= "debug";

	/**
	 * 存放在Request param中的adapter变量
	 */
	protected static final String	PARAM_ADAPTER	= "key";

	@Override
	public ModelAndView handleException(Exception ex, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (ex instanceof ValueListException) {
			return handleValueListException((ValueListException) ex, req, res);
		} else {
			return super.handleException(ex, req, res);
		}
	}

	/**
	 * 处理Controller方法中抛出的ValueList异常
	 * 
	 * @param ae Ajax异常
	 * @param res
	 * @return
	 * @author 黄国庆 2012-1-29 下午10:04:35
	 */
	protected ModelAndView handleValueListException(ValueListException ve, HttpServletRequest req, HttpServletResponse res) {
		if (isAjaxRequest(req)) {
			return handleAjaxException(new AjaxException(ve), res);
		}
		ModelAndView modelAndView = getErrorView(req);
		modelAndView.getModel().put("exception", getMessage(ve));
		modelAndView.getModel().put("url", req.getRequestURL().toString());
		return modelAndView;
	}

	/**
	 * 根据request域中param，获取valuelist QureyMap，并生成ValueListInfo实例，
	 * 设置ValueListInfo实例属性，查询数据库获取ValueList实例
	 * 
	 * @param adapter valuelist Adapter 名称
	 * @param req
	 * @see org.webframe.web.valuelist.ValueListUtils
	 * @return
	 * @author 黄国庆 2011-4-25 下午08:38:10
	 */
	protected ValueList<?> getValueList(String adapter, HttpServletRequest req) {
		if (req.getParameter(PARAM_DEBUG) != null) {
			reloadValueListSpringContext();
		}
		if (adapter == null || "".equals(adapter)) {
			throw new NullPointerException("没有指定Valuelist Adapter!");
		}
		try {
			return ValueListUtils.getValueList(adapter, req);
		} catch (Exception e) {
			throw new ValueListException("Adapter Name(" + adapter + ")：", e);
		}
	}

	/**
	 * HttpServletRequest中需要包括参数key不为空的值，抛出NullPointerException异常
	 * 
	 * @param req
	 * @return
	 * @author 黄国庆 2012-1-30 上午10:18:13
	 */
	protected ValueList<?> getValueList(HttpServletRequest req) {
		return getValueList(req.getParameter(PARAM_ADAPTER), req);
	}

	/**
	 * 重新加载valuelist配置文件
	 * 
	 * @author 黄国庆 2012-1-30 上午10:20:51
	 */
	protected void reloadValueListSpringContext() {
		ValueListUtils.reloadValueListSpringContext();
	}
}
