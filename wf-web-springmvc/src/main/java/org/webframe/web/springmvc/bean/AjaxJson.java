/*
 * com.berheley.bi.basic
 * Created on 2011-12-4-上午11:04:44
 */

package org.webframe.web.springmvc.bean;

import net.sf.json.JSONObject;

import org.webframe.support.util.SystemLogUtils;

/**
 * Ajax JOSN信息封装
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 下午09:52:35
 * @version
 */
public class AjaxJson {

	private JSONObject	json	= new JSONObject();

	public AjaxJson putMsg(String key, Object value) {
		json.put(key, value);
		return this;
	}

	public Object getMsg(String key) {
		return json.get(key);
	}

	@Override
	public String toString() {
		String str = json.toString();
		SystemLogUtils.println("AjaxJson:\n" + str);
		return str;
	}
}
