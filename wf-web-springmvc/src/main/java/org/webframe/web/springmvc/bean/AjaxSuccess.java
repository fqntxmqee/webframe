/*
 * wf-web-springmvc
 * Created on 2012-1-30-上午08:30:09
 */

package org.webframe.web.springmvc.bean;

/**
 * Ajax成功信息，格式：{success:true, msg:{brief: ""}}
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 上午08:30:09
 * @version
 */
public class AjaxSuccess extends AjaxJson {

	/**
	 * 带成功信息的json串
	 * 
	 * @param brief 成功信息
	 */
	public AjaxSuccess(String brief) {
		super(true, brief);
	}

	public AjaxSuccess putSuccess(String key, String value) {
		addMsg(key, value);
		return this;
	}
}
