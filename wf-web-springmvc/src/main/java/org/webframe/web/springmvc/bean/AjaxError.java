/*
 * com.berheley.bi.basic
 * Created on 2011-12-4-上午10:23:58
 */

package org.webframe.web.springmvc.bean;

/**
 * Ajax 错误信息，格式：{success:false, msg:{brief: "", detail: ""}}
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 上午08:30:16
 * @version
 */
public class AjaxError extends AjaxJson {

	public AjaxError(String brief) {
		super(false, brief);
	}

	public AjaxError(String brief, String detail) {
		super(false, brief);
		addDetailMsg(detail);
	}

	public void addDetailMsg(String value) {
		addMsg("detail", value);
	}

	public AjaxError putError(String key, String value) {
		addMsg(key, value);
		return this;
	}
}
