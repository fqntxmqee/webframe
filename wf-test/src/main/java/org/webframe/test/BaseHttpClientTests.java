
package org.webframe.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.webframe.test.web.BaseWebServerTests;

/**
 * 基于web容器（jetty）环境的测试用例基类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-28 下午08:51:08
 */
public class BaseHttpClientTests extends BaseWebServerTests {

	protected final static String					defaultEncode	= org.apache.http.protocol.HTTP.UTF_8;

	protected final static DefaultHttpClient	client			= new DefaultHttpClient();

	private HttpContext								context			= null;

	private static Map<String, Boolean>			userOnceMap		= new HashMap<String, Boolean>();

	protected String getBaseUrl() {
		return getServerURL();
	}

	/**
	 * 获取url全路径包括主机和端口，默认使用http；
	 * 
	 * @param url
	 * @return
	 * @author 黄国庆 2012-2-6 上午08:25:29
	 */
	protected String getUrl(String url) {
		String baseUrl = getBaseUrl();
		if (url == null) {
			return baseUrl;
		}
		if (url.toLowerCase().startsWith("http:") || url.toLowerCase().startsWith("https:")) {
			baseUrl = "";
		}
		return baseUrl + url;
	}

	/**
	 * 通过url获取Get请求对象
	 * 
	 * @param url
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:17:27
	 */
	protected final HttpGet getHttpGet(String url) {
		return new HttpGet(getUrl(url));
	}

	/**
	 * 通过url获取Post请求对象
	 * 
	 * @param url
	 * @return
	 * @author 黄国庆 2012-1-26 上午09:17:51
	 */
	protected final HttpPost getHttpPost(String url) {
		return new HttpPost(getUrl(url));
	}

	/**
	 * 发送Get请求对象到服务器，返回字符串
	 * 
	 * @param url 字符串上可以包含参数，例如：/login?username=admin&password=1
	 * @return 字符串内容结尾包含'\r\n'
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:18:28
	 */
	protected String sendGet(String url) throws Exception {
		return getResponseContent(executeGet(url));
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
	protected String sendPost(String url, Map<String, Object> params) throws Exception {
		// 取得返回的字符串
		return sendPost(url, getPairs(params));
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
	protected String sendPost(String url, List<NameValuePair> params) throws Exception {
		return getResponseContent(executePost(url, params));
	}

	/**
	 * 发送Get请求对象到服务器，返回HttpResponse
	 * 
	 * @param url 字符串上可以包含参数，例如：/login?username=admin&password=1
	 * @return HttpResponse对象
	 * @throws Exception
	 * @author 黄国庆 2012-1-26 上午09:23:49
	 */
	protected HttpResponse executeGet(String url) throws Exception {
		// HttpPost连接对象
		HttpGet httpRequest = getHttpGet(url);
		return client.execute(httpRequest, context);
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
	protected HttpResponse executePost(String url, Map<String, Object> params) throws Exception {
		return executePost(url, getPairs(params));
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
	protected HttpResponse executePost(String url, List<NameValuePair> params) throws Exception {
		// HttpPost连接对象
		HttpPost httpRequest = getHttpPost(url);
		// 设置字符集
		HttpEntity httpentity = new UrlEncodedFormEntity(params, defaultEncode);
		// 请求httpRequest
		httpRequest.setEntity(httpentity);
		return client.execute(httpRequest, context);
	}

	protected String getResponseContent(HttpResponse res) throws Exception {
		HttpEntity httpEntity = res.getEntity();
		/*
		 *  取得返回的字符串，EntityUtils.toString方法会关闭httpEntity中的InputStream流，
		 *  不需要再使用EntityUtils.consume(entity);
		 */
		return EntityUtils.toString(httpEntity, defaultEncode);
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

	protected void setContext(HttpContext context) {
		this.context = context;
	}

	protected final HttpContext getContext() {
		return this.context;
	}

	/**
	 * 开启安全认证后，可以访问受限制资源
	 * 
	 * @param usernamePassword 例如：'admin:123456'
	 * @author 黄国庆 2012-1-27 上午08:19:22
	 */
	protected final void enableAuth(String usernamePassword) {
		client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
			new UsernamePasswordCredentials(usernamePassword));
	}

	/**
	 * 使用默认的登陆链接进行登陆：url("/j_spring_security_check"), user("admin:1")
	 * 
	 * @throws Exception
	 * @author 黄国庆 2012-2-6 上午08:45:58
	 */
	protected void login() throws Exception {
		login("/j_spring_security_check", "admin:1");
	}

	/**
	 * 执行登陆方法后，就有权限方法其他资源
	 * 
	 * @param loginUrl 登陆url，例如：'/j_spring_security_check'
	 * @param usernamePassword 用户信息，例如："admin:1"
	 * @throws Exception
	 * @author 黄国庆 2012-2-6 上午08:45:16
	 */
	protected void login(String loginUrl, String usernamePassword) throws Exception {
		if (usernamePassword == null) {
			throw new IllegalArgumentException("Username:password string may not be null!");
		}
		Map<String, Object> user = new HashMap<String, Object>();
		int index = usernamePassword.indexOf(':');
		if (index >= 0) {
			user.put("j_username", usernamePassword.substring(0, index));
			user.put("j_password", usernamePassword.substring(index + 1));
		} else {
			user.put("j_username", "usernamePassword");
			user.put("j_password", null);
		}
		login(loginUrl, user);
	}

	/**
	 * 执行登陆方法后，就有权限方法其他资源
	 * 
	 * @param loginUrl 登陆url，例如：'/j_spring_security_check'
	 * @param user 包含用户信息的Map，例如：user.put("j_username", "admin");user.put("j_password", "1");
	 * @throws Exception
	 * @author 黄国庆 2012-2-6 上午08:38:11
	 */
	protected void login(String loginUrl, Map<String, Object> user) throws Exception {
		if (user == null) {
			throw new IllegalArgumentException("user map may not be null!");
		}
		if (userOnceMap.get(user.toString()) != null) {
			return;
		}
		HttpResponse response = executePost(loginUrl, user);
		EntityUtils.consume(response.getEntity());
		userOnceMap.put(user.toString(), true);
	}
}
