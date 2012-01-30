/*
 * wf-web-springmvc
 * Created on 2012-1-29-下午09:46:44
 */

package org.webframe.web.springmvc.exp;

import org.webframe.web.springmvc.bean.AjaxError;

/**
 * AjaxException
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 下午09:46:44
 * @version
 */
public class AjaxException extends RuntimeException {

	private AjaxError				ajaxError			= null;

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8179866059145520037L;

	public AjaxException(String msg) {
		super(msg);
	}

	public AjaxException(Throwable cause) {
		super(cause);
	}

	public AjaxException(AjaxError ajaxError, String msg) {
		super(msg);
		this.ajaxError = ajaxError;
	}

	public AjaxException(AjaxError ajaxError, Throwable cause) {
		super(cause);
		this.ajaxError = ajaxError;
	}

	public AjaxError getAjaxError() {
		if (ajaxError == null) {
			this.ajaxError = new AjaxError();
			this.ajaxError.putError("brief", "服务器异常，请联系管理员！");
		}
		return ajaxError;
	}
}
