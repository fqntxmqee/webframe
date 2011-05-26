/*
 * wf-web
 * Created on 2011-5-26-下午01:51:53
 */

package org.webframe.web;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.webframe.support.util.SystemLogUtils;
import org.webframe.test.BaseHttpClientTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-26 下午01:51:53
 */
public class WebIT extends BaseHttpClientTests {

	@Test
	public void testFilterProxy() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:8080/myfilter?name=黄国庆");
		HttpResponse response = client.execute(httpGet);
		assertEquals(200, response.getStatusLine().getStatusCode());
		// 取得返回的字符串
		String strResult = EntityUtils.toString(response.getEntity());
		SystemLogUtils.println(strResult);
	}
}
