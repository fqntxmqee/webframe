/*
 * wf-test
 * Created on 2011-12-31-上午09:59:09
 */

package org.webframe.test.web;

import org.junit.runner.RunWith;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 上午09:59:09
 */
@RunWith(WFJettyJUnit4Runner.class)
public class BaseWebServerTests {

	private static Server	server	= WFJettyJUnit4Runner.server;

	protected String			HTTP		= "http";

	protected String			HTTPS		= "https";

	// 用tomcat容器测试时，需要初始化该值
	static int					port		= 0;

	// 用tomcat容器测试时，需要初始化该值
	static String				host		= "";

	protected Connector disposeConnector() {
		Connector[] connectors = server.getConnectors();
		return connectors[0];
	}

	protected int getServerPort() {
		if (server != null) {
			Connector connector = disposeConnector();
			return connector.getPort();
		} else {
			return port;
		}
	}

	protected String getServerHost() {
		if (server != null) {
			Connector connector = disposeConnector();
			return connector.getHost();
		} else {
			return host;
		}
	}

	protected String getServerURL() {
		return getServerURL(HTTP);
	}

	protected String getServerURL(String protocol) {
		return protocol + "://" + getServerHost() + ":" + getServerPort();
	}
}
