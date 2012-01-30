/*
 * wf-web-springmvc
 * Created on 2012-1-30-下午09:41:33
 */

package org.webframe.web.springmvc.controller;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.sf.json.JSONArray;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.webframe.test.BaseHttpClientTests;

/**
 * BaseValueListController测试类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午09:41:33
 * @version
 */
public class BaseValueListControllerTest extends BaseHttpClientTests {

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseValueListController#handleValueListException(org.webframe.web.page.exp.ValueListException, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHandleValueListException() throws Exception {
		HttpResponse response = executeGet("/test/valuelistExp");
		Assert.assertEquals("ValueListException异常捕获失败！", 500, response.getStatusLine().getStatusCode());
		String resStr = EntityUtils.toString(response.getEntity(), defaultEncode);
		Assert.assertTrue("ValueListException异常捕获失败！", resStr.contains("测试ValueListException异常"));
	}

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseValueListController#getValueList(javax.servlet.http.HttpServletRequest)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetValueListHttpServletRequest() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key", "testHql");
		params.put("attribute(age)", 25);
		params.put("attribute(enable)", false);
		String json = sendPost("/test/getValueList", params);
		JSONArray jsonArray = JSONArray.fromObject(json);
		Assert.assertEquals("getValueList获取数据失败！", 0, jsonArray.size());
	}
}
