/*
 * wf-web-springmvc
 * Created on 2012-1-30-上午08:30:09
 */

package org.webframe.web.springmvc.bean;

import net.sf.json.JSONObject;

/**
 * Ajax成功信息
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 上午08:30:09
 * @version
 */
public class AjaxSuccess extends AjaxJson {

	private JSONObject	success	= null;

	public AjaxSuccess() {
	}

	public AjaxSuccess(JSONObject success) {
		this.success = success;
	}

	public AjaxSuccess putSuccess(String key, String success) {
		createSuccess();
		this.success.put(key, success);
		return this;
	}

	protected JSONObject createSuccess() {
		if (this.success == null) this.success = new JSONObject();
		return this.success;
	}

	@Override
	public String toString() {
		createSuccess();
		putMsg("success", success);
		return super.toString();
	}
}
