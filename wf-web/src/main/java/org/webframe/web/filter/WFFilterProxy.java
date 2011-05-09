
package org.webframe.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-21 上午10:49:26
 */
public class WFFilterProxy extends DelegatingFilterProxy {

	private static final String	BEAN_NAME_WEBFRAME_FILTER_CONTEXT		= "webframeFilterContext";

	private static final String	BEAN_NAME_SPRING_SECURITY_FILTER_CHAIN	= "springSecurityFilterChain";

	private final Object				delegateMonitor								= new Object();

	WFFilterContext					webframeFilterContext;

	boolean								hasSecurity										= true;

	@Override
	protected void initFilterBean() throws ServletException {
		// If no target bean name specified, use filter name.
		if (getTargetBeanName() == null) {
			setTargetBeanName(BEAN_NAME_SPRING_SECURITY_FILTER_CHAIN);
		}
		synchronized (this.delegateMonitor) {
			initWebFrameFilterContext();
		}
		try {
			super.initFilterBean();
		} catch (BeansException e) {
			hasSecurity = false;
			logger.error(getTargetBeanName() + "没有注入，系统没有Security！");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		List<Filter> filters = webframeFilterContext.getBeforeSecurity();
		new WFFilterChain(filterChain, filters).doFilter(request, response, this);
	}

	public void superDoFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		super.doFilter(request, response, filterChain);
	}

	public FilterChain afterSecurityFilterChain(ServletRequest request, ServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		List<Filter> filters = webframeFilterContext.getAfterSecurity();
		return new WFFilterChain(filterChain, filters);
	}

	protected void initWebFrameFilterContext() {
		WebApplicationContext wac = findWebApplicationContext();
		webframeFilterContext = wac.getBean(BEAN_NAME_WEBFRAME_FILTER_CONTEXT, WFFilterContext.class);
	}

	@Override
	public void destroy() {
		webframeFilterContext.destroy();
		super.destroy();
	}
}
