
package org.webframe.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.webframe.test.web.BaseWebServerTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-28 下午08:51:08
 */
public class BaseHttpClientTests extends BaseWebServerTests {

	protected final static String			defaultEncode	= org.apache.http.protocol.HTTP.UTF_8;

	protected final static HttpClient	client			= new DefaultHttpClient();

	protected String getBaseUrl() {
		return getServerURL();
	}

	/**
	 * 通过url获取Get请求对象
	 * 
	 * @param url
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:17:27
	 */
	protected final HttpGet getHttpGet(String url) {
		return new HttpGet(getBaseUrl() + url);
	}

	/**
	 * 通过url获取Post请求对象
	 * 
	 * @param url
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:17:51
	 */
	protected final HttpPost getHttpPost(String url) {
		return new HttpPost(getBaseUrl() + url);
	}

	/**
	 * 发送Get请求对象到服务器，返回字符串
	 * 
	 * @param url 字符串上可以包含参数，例如：/login?username=admin&password=1
	 * @return 字符串内容结尾包含'\r\n'
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:18:28
	 */
	protected String getSend(String url) throws Exception {
		HttpEntity httpEntity = getExcute(url).getEntity();
		// 取得返回的字符串
		return EntityUtils.toString(httpEntity, defaultEncode);
	}

	/**
	 * 发送Post请求对象到服务器，返回字符串
	 * 
	 * @param url 不包含参数的url路径
	 * @param params Map参数集合
	 * @return 字符串内容结尾包含'\r\n'
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:20:01
	 */
	protected String postSend(String url, Map<String, Object> params) throws Exception {
		// 取得返回的字符串
		return postSend(url, getPairs(params));
	}

	/**
	 * 发送Post请求对象到服务器，返回字符串
	 * 
	 * @param url 不包含参数的url路径
	 * @param params NameValuePair List参数集合
	 * @return 字符串内容结尾包含'\r\n'
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:22:38
	 */
	protected String postSend(String url, List<NameValuePair> params) throws Exception {
		HttpEntity httpEntity = postExcute(url, params).getEntity();
		// 取得返回的字符串
		return EntityUtils.toString(httpEntity, defaultEncode);
	}

	/**
	 * 发送Get请求对象到服务器，返回HttpResponse
	 * 
	 * @param url 字符串上可以包含参数，例如：/login?username=admin&password=1
	 * @return HttpResponse对象
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:23:49
	 */
	protected HttpResponse getExcute(String url) throws Exception {
		// HttpPost连接对象
		HttpGet httpRequest = getHttpGet(url);
		return client.execute(httpRequest);
	}

	/**
	 * 发送Post请求对象到服务器，返回HttpResponse
	 * 
	 * @param url 不包含参数的url路径
	 * @param params Map参数集合
	 * @return HttpResponse对象
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:25:30
	 */
	protected HttpResponse postExcute(String url, Map<String, Object> params) throws Exception {
		return postExcute(url, getPairs(params));
	}

	/**
	 * 发送Post请求对象到服务器，返回HttpResponse
	 * 
	 * @param url 不包含参数的url路径
	 * @param params NameValuePair List参数集合
	 * @return HttpResponse对象
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:26:44
	 */
	protected HttpResponse postExcute(String url, List<NameValuePair> params) throws Exception {
		// HttpPost连接对象
		HttpPost httpRequest = getHttpPost(url);
		// 设置字符集
		HttpEntity httpentity = new UrlEncodedFormEntity(params, defaultEncode);
		// 请求httpRequest
		httpRequest.setEntity(httpentity);
		return client.execute(httpRequest);
	}

	/**
	 * 获取参数的字符串格式
	 * 
	 * @param params Map参数集合
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:30:40
	 */
	protected String getUrlParamsString(Map<String, Object> params) {
		return getUrlParamsString(getPairs(params));
	}

	/**
	 * 获取参数的字符串格式
	 * 
	 * @param params NameValuePair List参数集合
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:30:46
	 */
	protected String getUrlParamsString(List<NameValuePair> params) {
		return URLEncodedUtils.format(params, defaultEncode);
	}

	/**
	 * Map参数集合转换为NameValuePair List参数集合
	 * 
	 * @param params Map参数集合
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:25:59
	 */
	protected final List<NameValuePair> getPairs(Map<String, Object> params) {
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (params != null) {
			// 添加要传递的参数
			for (String key : params.keySet()) {
				Object value = params.get(key);
				String sv = value == null ? "null" : value.toString();
				nameValuePairs.add(new BasicNameValuePair(key, sv));
			}
		}
		return nameValuePairs;
	}
}
