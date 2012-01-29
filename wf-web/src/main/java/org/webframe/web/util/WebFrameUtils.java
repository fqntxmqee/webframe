
package org.webframe.web.util;

import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.webframe.web.spring.ServiceHelper;

/**
 * webframe框架的工具类，可以获取spring框架中注入的Bean，动态注入Bean；当在Web应用中运行，可以获取Web应用的上下文、项目的物理位置
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:28:57
 * @version
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
