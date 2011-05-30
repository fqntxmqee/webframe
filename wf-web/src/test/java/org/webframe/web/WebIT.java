/*
 * wf-web
 * Created on 2011-5-26-下午01:51:53
 */

package org.webframe.web;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.webframe.support.util.SystemLogUtils;
import org.webframe.test.BaseHttpClientTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-26 下午01:51:53
 */
public class WebIT extends BaseHttpClientTests {

	private String	defaultEncode	= "UTF-8";

	@Test
	public void testFilterProxy() throws Exception {
		String httpUrl = "http://localhost:8080/myfilter";
		// HttpPost连接对象
		HttpPost httpRequest = new HttpPost(httpUrl);
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加要传递的参数
		params.add(new BasicNameValuePair("name", "黄国庆"));
		// 设置字符集
		HttpEntity httpentity = new UrlEncodedFormEntity(params, defaultEncode);
		// 请求httpRequest
		httpRequest.setEntity(httpentity);
		HttpResponse response = client.execute(httpRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
		// 取得返回的字符串
		String strResult = EntityUtils.toString(response.getEntity(), defaultEncode);
		SystemLogUtils.println(strResult);
	}
}
