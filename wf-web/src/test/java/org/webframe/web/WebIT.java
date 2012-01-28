/*
 * wf-web
 * Created on 2011-5-26-下午01:51:53
 */

package org.webframe.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.webframe.support.util.SystemLogUtils;
import org.webframe.test.BaseHttpClientTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-26 下午01:51:53
 */
public class WebIT extends BaseHttpClientTests {

	private static final String	name	= "Filter 测试！";

	@Test
	public void testFilterProxy() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		// 取得返回的字符串
		String str = sendGet("/myfilter?" + getUrlParamsString(params));
		SystemLogUtils.println(str);
		Assert.assertTrue("MyFilter 未加载！", name.equals(str));
	}
}
