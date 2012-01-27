/*
 * wf-test
 * Created on 2011-12-31-上午09:59:09
 */

package org.webframe.test.web;

import org.junit.runner.RunWith;
import org.mortbay.jetty.Connector;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 上午09:59:09
 */
@RunWith(WFJettyJUnit4Runner.class)
public class BaseWebServerTests {

	protected static final String	HTTP	= "http";

	protected static final String	HTTPS	= "https";

	// 用tomcat容器测试时，需要初始化该值
	static int							port	= 0;

	// 用tomcat容器测试时，需要初始化该值
	static String						host	= "";

	protected Connector disposeConnector() {
		Connector[] connectors = WFJettyJUnit4Runner.server.getConnectors();
		return connectors[0];
	}

	protected int getServerPort() {
		if (WFJettyJUnit4Runner.server != null) {
			Connector connector = disposeConnector();
			return connector.getPort();
		} else {
			return port;
		}
	}

	protected String getServerHost() {
		if (WFJettyJUnit4Runner.server != null) {
			Connector connector = disposeConnector();
			return connector.getHost();
		} else {
			return host;
		}
	}

	/**
	 * 默认获取http协议的ServerURL
	 * 
	 * @return
	 * @author 黄国庆 2012-1-26 下午09:21:01
	 */
	protected String getServerURL() {
		return getServerURL(HTTP);
	}

	/**
	 * 指定URL协议类型，获取URL
	 * 
	 * @param protocol {@link org.webframe.test.web.BaseWebServerTests.HTTP} 或
	 *           {@link org.webframe.test.web.BaseWebServerTests.HTTPS}
	 * @return
	 * @author 黄国庆 2012-1-26 下午09:21:51
	 */
	protected String getServerURL(String protocol) {
		return protocol + "://" + getServerHost() + ":" + getServerPort();
	}
}
