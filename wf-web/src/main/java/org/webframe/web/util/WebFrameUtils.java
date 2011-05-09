
package org.webframe.web.util;

import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.webframe.web.spring.ServiceHelper;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-3 上午07:41:19
 */
public final class WebFrameUtils extends ServiceHelper {

	private static String	webContextPath	= null;

	private static String	webRealPath		= null;

	public static Object getBean(String beanName) {
		return getService(beanName);
	}

	public static String getWebContextPath() {
		if (webContextPath != null) return webContextPath;
		if (getApplicationContext() instanceof AbstractRefreshableWebApplicationContext) {
			AbstractRefreshableWebApplicationContext bwc = (AbstractRefreshableWebApplicationContext) getApplicationContext();
			webContextPath = bwc.getServletContext().getContextPath();
		}
		return webContextPath;
	}

	public static String getWebRealPath() {
		if (webRealPath != null) return webRealPath;
		if (getApplicationContext() instanceof AbstractRefreshableWebApplicationContext) {
			AbstractRefreshableWebApplicationContext bwc = (AbstractRefreshableWebApplicationContext) getApplicationContext();
			webRealPath = bwc.getServletContext().getRealPath("/");
		}
		return webRealPath;
	}
}
