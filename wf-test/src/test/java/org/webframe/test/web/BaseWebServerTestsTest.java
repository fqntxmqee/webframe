/*
 * wf-test
 * Created on 2011-12-31-下午02:06:45
 */

package org.webframe.test.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.webframe.support.util.SystemLogUtils;
import org.webframe.test.BaseHttpClientTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 下午02:06:45
 */
public class BaseWebServerTestsTest extends BaseHttpClientTests {

	private String	role	= "ROLE_TEST";

	private String	label	= "呼呼";

	public BaseWebServerTestsTest() throws Exception {
		login();
	}

	@Test
	public void testHttpGet() throws Exception {
		enableAuth("admin:1");
		Map<String, Object> params = new HashMap<String, Object>();
		// 添加要传递的参数
		params.put(TestServlet.ROLE, role);
		params.put(TestServlet.LABEL, label);
		// 取得返回的字符串
		String strResult = sendGet("/test?" + getUrlParamsString(params));
		SystemLogUtils.println(strResult);
		Assert.assertTrue("Get请求失败", strResult.startsWith(("GET:" + role + label)));
	}

	@Test
	public void testHttpPost() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		// 添加要传递的参数
		params.put(TestServlet.ROLE, role);
		params.put(TestServlet.LABEL, label);
		String strResult = sendPost("/test", params);
		SystemLogUtils.println(strResult);
		Assert.assertTrue("Post请求失败", strResult.startsWith(("POST:" + role + label)));
	}
}
