/*
 * com.berheley.bi.basic
 * Created on 2011-12-4-上午11:04:44
 */

package org.webframe.web.springmvc.bean;

import java.util.HashSet;
import java.util.Set;

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

	private final JSONObject	json				= new JSONObject();

	private final Set<String>	protectedKeys	= new HashSet<String>(2);

	private JSONObject			msg				= null;

	public AjaxJson() {
		protectedKeys.add("success");
		protectedKeys.add("msg");
	}

	protected AjaxJson(boolean success, String brief) {
		this();
		json.put("success", success);
		if (msg == null) msg = new JSONObject();
		addMsg("brief", brief);
	}

	public AjaxJson put(String key, Object value) {
		if (validate(key)) throw new IllegalArgumentException(key + " 受保护，不能put！");
		json.put(key, value);
		return this;
	}

	public Object get(String key) {
		return json.get(key);
	}

	protected void addMsg(String key, Object value) {
		msg.put(key, value);
	}

	private boolean validate(String key) {
		return protectedKeys.contains(key);
	}

	@Override
	public String toString() {
		if (msg != null) json.put("msg", msg);
		String str = json.toString();
		SystemLogUtils.println("AjaxJson:\n" + str);
		return str;
	}
}
