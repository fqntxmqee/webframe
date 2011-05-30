
package org.webframe.struts.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.springframework.web.util.WebUtils;
import org.webframe.core.exception.ServiceException;
import org.webframe.web.util.WebFrameUtils;

/**
 * This class is:Action的基类，所有的Action继承它 这是一个dispatchAction,便于把独立的功能放在一起。
 * 主要完成：具体Action的调用，然后统一处理异常，以及出现异常时的消息显示。
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-30 上午09:54:04
 */
public class BaseAction extends DispatchAction {

	private final static String	ACTION_PARAMETER	= "method";

	// log
	protected Log						log					= LogFactory.getLog(getClass());

	protected final static String	FORWARD_AJAX		= "ajax";

	protected final static String	FORWARD_FAIL		= "fail";

	// 消息参数
	protected ActionMessages		errors;

	protected ActionMessages		messages;

	protected void addErrors(String key) {
		this.addErrors(key, null, null);
	}

	/**
	 * @param key Message key for this error message
	 * @param value First replacement value
	 */
	protected void addErrors(String key, String value) {
		this.addErrors(key, value, null);
	}

	/**
	 * @param key Message key for this error message
	 * @param value First replacement value
	 * @param property Property name (or ActionMessages.GLOBAL_MESSAGE)
	 */
	protected void addErrors(String key, String value, String property) {
		errors = (errors == null) ? new ActionMessages() : errors;
		if (property == null) {
			errors.add(value, (value == null) ? new ActionMessage(key) : new ActionMessage(key, value));
		} else {
			errors.add(property, (value == null) ? new ActionMessage(key) : new ActionMessage(key, value));
		}
	}

	protected void addMessages(String key) {
		this.addMessages(key, null, null);
	}

	/**
	 * @param key Message key for this message
	 * @param value First replacement value
	 */
	protected void addMessages(String key, String value) {
		this.addMessages(key, value, null);
	}

	/**
	 * @param key Message key for this message
	 * @param value First replacement value
	 * @param property Property name (or ActionMessages.GLOBAL_MESSAGE)
	 */
	protected void addMessages(String key, String value, String property) {
		messages = (messages == null) ? new ActionMessages() : messages;
		if (property == null) {
			messages.add(value, (value == null) ? new ActionMessage(key) : new ActionMessage(key, value));
		} else {
			messages.add(property, (value == null) ? new ActionMessage(key) : new ActionMessage(key, value));
		}
	}

	@Override
	protected String getParameter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		// Identify the request parameter containing the method name
		String parameter = mapping.getParameter();
		// 覆盖父类getParameter方法，主要是简化struts-config.xml中的parameter，配置
		if (parameter == null || "".equals(parameter)) {
			parameter = ACTION_PARAMETER;
		}
		return parameter;
	}

	/**
	 * Action的执行函数。从这个函数进入具体的action.返回后在这里处理异常
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
				throws UnsupportedEncodingException {
		// 将参数初始化
		errors = new ActionMessages();
		messages = new ActionMessages();
		ActionForward actionForward = new ActionForward();
		HttpSession session = req.getSession();
		try {
			// 执行实际的Action
			actionForward = super.execute(mapping, form, req, res);
			// 显示请求的相关信息，便于开发和测试
			showRequestInfo(mapping, req, actionForward);
			if (actionForward == null) {
				// ajax申请返回
				if (req.getAttribute(FORWARD_AJAX) != null && req.getAttribute(FORWARD_AJAX).toString().equals("true")) {
					return null;
				}
				// 如果返回的actionForward对象为空，表示是重复提交
				actionForward = (ActionForward) session.getAttribute("forward");
				errors = (ActionMessages) session.getAttribute("errors");
				messages = (ActionMessages) session.getAttribute("messages");
			} else {
				// add by wangnan 解决forward转发请求时的消息问题
				if (actionForward.getPath().indexOf(".bc?" + ACTION_PARAMETER + "=") != -1) {
					req.setAttribute("ACTION_FORWARD_DO", "Y");
				} else {
					if ("Y".equals(req.getAttribute("ACTION_FORWARD_DO"))) {
						errors = (ActionMessages) req.getAttribute(Globals.ERROR_KEY);
						messages = (ActionMessages) req.getAttribute(Globals.MESSAGE_KEY);
					}
				}
				saveToken(req);
			}
		} catch (ServiceException e) {
			// 需要抛出给用户的异常
			actionForward = mapping.findForward(FORWARD_FAIL);
			// ValidateUtil.checkBusinessException(errors, e.getKeyValue(), e.getErrorCode());
			if (actionForward.getPath().indexOf(".wf?" + ACTION_PARAMETER + "=") != -1) {
				req.setAttribute("ACTION_FORWARD_DO", "Y");
			}
		} catch (NoSuchMethodException e) {
			// Action中没有相应的方法
			actionForward = (ActionForward) session.getAttribute("forward");
			errors = (ActionMessages) session.getAttribute("errors");
			messages = (ActionMessages) session.getAttribute("messages");
		} catch (Exception e) {
			// 不需要显示原因的异常
			actionForward = mapping.findForward(FORWARD_FAIL);
			// ValidateUtil.checkSysException(errors);
			log.error(e);
			e.printStackTrace();
		}
		// 保存状态，如果重复提交则给出这些信息
		session.setAttribute("forward", actionForward);
		session.setAttribute("messages", messages);
		session.setAttribute("errors", errors);
		// 返回
		saveErrors(req, errors);
		saveMessages(req, messages);
		return actionForward;
	}

	/**
	 * @function:显示请求的相关信息
	 * @param mapping
	 * @param req
	 * @param actionForward
	 * @author: zhaoxiaoqiang 2010-4-23 上午11:00:29
	 */
	private void showRequestInfo(ActionMapping mapping, HttpServletRequest req, ActionForward actionForward) {
		if (log.isInfoEnabled()) {
			String uri = req.getRequestURI();
			String method = req.getParameter("method");
			String path = "ajax请求！";
			if (null != actionForward && null != actionForward.getPath()) {
				path = actionForward.getPath();
			}
			log.info("请求的路径：" + uri);
			log.info("请求的方法：" + method);
			log.info("对应的Action：" + mapping.getType());
			log.info("返回的页面：" + path);
		}
	}

	/**
	 * spring 获取注入的bean
	 * 
	 * @param serviceBeanName
	 * @return
	 */
	protected Object getService(String serviceBeanName) {
		log.info("return serivce object named:" + serviceBeanName);
		return WebFrameUtils.getBean(serviceBeanName);
	}

	/**
	 * Return the temporary directory for the current web application, as provided by the servlet
	 * container.
	 * 
	 * @return the File representing the temporary directory
	 */
	protected final File getTempDir() {
		return WebUtils.getTempDir(super.getServlet().getServletContext());
	}

	/**
	 * @param request
	 * @param response
	 * @param resStr 例如: "{message:\"消息内容！\", state:\"error\"}"
	 * @throws IOException
	 * @function: ajax返回json数据
	 * @author: 黄国庆 2008-7-16 上午11:10:26
	 */
	protected ActionForward ajaxReturnJSON(HttpServletRequest request, HttpServletResponse response, String resStr)
				throws IOException {
		return this.ajaxReturn(request, response, resStr, false);
	}

	/**
	 * @param request
	 * @param response
	 * @param resStr
	 * @throws IOException
	 * @function: ajax返回xml数据
	 * @author: 黄国庆 2008-7-16 上午11:10:26
	 */
	protected ActionForward ajaxReturnXML(HttpServletRequest request, HttpServletResponse response, String resStr)
				throws IOException {
		return this.ajaxReturn(request, response, resStr, true);
	}

	@Override
	protected void saveErrors(HttpServletRequest request, ActionMessages errors) {
		if (errors != null && !errors.isEmpty()) {
			super.saveErrors(request, errors);
		}
	}

	@Override
	protected void saveMessages(HttpServletRequest request, ActionMessages messages) {
		if (messages != null && !messages.isEmpty()) {
			super.saveMessages(request, messages);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param resStr
	 * @param isXml ajax返回类型, true表示xml
	 * @throws IOException
	 * @function: ajax返回xml数据
	 * @author: 黄国庆 2008-7-16 上午11:10:26
	 */
	private ActionForward ajaxReturn(HttpServletRequest request, HttpServletResponse response, String resStr, boolean isXml)
				throws IOException {
		request.setAttribute(FORWARD_AJAX, "true");
		response.setCharacterEncoding("UTF-8");
		if (isXml) {
			response.setContentType("text/xml");
		}
		PrintWriter out = response.getWriter();
		out.print(resStr);
		out.flush();
		return null;
	}
}
