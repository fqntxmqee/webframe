/*
 * wf-web-springmvc
 * Created on 2012-2-17-下午02:38:08
 */

package org.webframe.web.springmvc.bean;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

/**
 * AjaxSuccessTest
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-17 下午02:38:08
 * @version
 */
public class AjaxSuccessTest {

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.bean.AjaxSuccess#putSuccess(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testPutSuccess() {
		String expected = "成功操作！";
		AjaxSuccess success = new AjaxSuccess(expected);
		JSONObject json = JSONObject.fromObject(success.toString());
		String actual = json.getJSONObject("msg").getString("brief");
		Assert.assertEquals("AjaxSuccess 解析错误", true, json.get("success"));
		Assert.assertEquals("AjaxSuccess 解析错误", expected, actual);
	}
}
