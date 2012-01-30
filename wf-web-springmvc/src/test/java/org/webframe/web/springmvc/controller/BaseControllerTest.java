/*
 * wf-web-springmvc
 * Created on 2012-1-30-下午08:07:47
 */

package org.webframe.web.springmvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.webframe.test.BaseHttpClientTests;
import org.webframe.web.util.PatternUtil;

/**
 * BaseController测试类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午08:07:47
 * @version
 */
public class BaseControllerTest extends BaseHttpClientTests {

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseController#handleException(java.lang.Exception, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public void testHandleException() throws Exception {
		HttpResponse response = executeGet("/test/exp");
		Assert.assertEquals("Exception异常捕获失败！", 500, response.getStatusLine().getStatusCode());
		String resStr = EntityUtils.toString(response.getEntity(), defaultEncode);
		Assert.assertTrue("Exception异常捕获失败！", resStr.contains("测试Exception异常"));
	}

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseController#handleServiceException(org.webframe.core.exception.ServiceException, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public void testHandleServiceException() throws Exception {
		HttpResponse response = executeGet("/test/serviceExp");
		Assert.assertEquals("Service异常捕获失败！", 500, response.getStatusLine().getStatusCode());
		String resStr = EntityUtils.toString(response.getEntity(), defaultEncode);
		Assert.assertTrue("Service异常捕获失败！", resStr.contains("测试Service异常"));
	}

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseController#handleAjaxException(org.webframe.web.springmvc.exp.AjaxException, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public void testHandleAjaxException() throws Exception {
		HttpResponse response = executeGet("/test/ajaxExp");
		Assert.assertEquals("Ajax异常捕获失败！", 500, response.getStatusLine().getStatusCode());
		String json = EntityUtils.toString(response.getEntity(), defaultEncode);
		JSONObject jsonObject = JSONObject.fromObject(json);
		String detail = jsonObject.getJSONObject("error").getString("detail");
		Assert.assertTrue("Ajax异常捕获失败！", detail.contains("测试Ajax异常"));
	}

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseController#getQueryMap(javax.servlet.http.HttpServletRequest, java.lang.Class)}
	 * .
	 */
	@Test
	public void testGetQueryMapHttpServletRequestClassOfQextendsBaseEntity() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "呼呼！");
		params.put("attribute(age)", 25);
		params.put("attribute(enable)", false);
		String json = sendPost("/test/getQueryMapClazz", params);
		JSONObject jsonObject = JSONObject.fromObject(json);
		for (String key : params.keySet()) {
			List<String> mathsList = PatternUtil.matchs("attribute\\((\\S*)\\)", key);
			if (mathsList.isEmpty()) {
				Assert.assertEquals("getQueryMap获取参数失败！", params.get(key), jsonObject.get(key));
			} else {
				Assert.assertEquals("getQueryMap获取参数失败！", params.get(key), jsonObject.get(mathsList.get(0)));
			}
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.web.springmvc.controller.BaseController#getQueryMap(javax.servlet.http.HttpServletRequest)}
	 * .
	 */
	@Test
	public void testGetQueryMapHttpServletRequest() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "呼呼！");
		params.put("age", "25");
		params.put("attribute(grade)", "本科");
		String json = sendPost("/test/getQueryMap", params);
		JSONObject jsonObject = JSONObject.fromObject(json);
		for (String key : params.keySet()) {
			List<String> mathsList = PatternUtil.matchs("attribute\\((\\S*)\\)", key);
			if (mathsList.isEmpty()) {
				Assert.assertEquals("getQueryMap获取参数失败！", params.get(key), jsonObject.get(key));
			} else {
				Assert.assertEquals("getQueryMap获取参数失败！", params.get(key), jsonObject.get(mathsList.get(0)));
			}
		}
	}
}
