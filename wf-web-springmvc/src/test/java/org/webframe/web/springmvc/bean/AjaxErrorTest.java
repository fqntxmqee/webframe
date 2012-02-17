/*
 * wf-web-springmvc
 * Created on 2012-2-17-下午02:37:33
 */

package org.webframe.web.springmvc.bean;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

/**
 * AjaxErrorTest
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-17 下午02:37:33
 * @version
 */
public class AjaxErrorTest {

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.bean.AjaxError#addDetailMsg(java.lang.String)}.
	 */
	@Test
	public void testAddDetailMsg() {
		String expected = "test";
		AjaxError error = new AjaxError("服务器异常，请联系管理员！");
		error.addDetailMsg(new Exception(expected).getMessage());
		JSONObject json = JSONObject.fromObject(error.toString());
		String actual = json.getJSONObject("msg").getString("detail");
		Assert.assertEquals("AjaxError 解析错误", false, json.get("success"));
		Assert.assertEquals("AjaxError 解析错误", expected, actual);
	}
}
