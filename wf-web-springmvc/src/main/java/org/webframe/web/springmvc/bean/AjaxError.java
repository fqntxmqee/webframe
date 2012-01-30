/*
 * com.berheley.bi.basic
 * Created on 2011-12-4-上午10:23:58
 */

package org.webframe.web.springmvc.bean;

import net.sf.json.JSONObject;

/**
 * Ajax 错误信息
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 上午08:30:16
 * @version
 */
public class AjaxError extends AjaxJson {

	private JSONObject	error	= null;

	public AjaxError() {
	}

	public AjaxError(JSONObject error) {
		this.error = error;
	}

	public AjaxError putError(String key, String error) {
		createError();
		this.error.put(key, error);
		return this;
	}

	protected JSONObject createError() {
		if (this.error == null) this.error = new JSONObject();
		return this.error;
	}

	@Override
	public String toString() {
		createError();
		putMsg("error", error);
		return super.toString();
	}
}
