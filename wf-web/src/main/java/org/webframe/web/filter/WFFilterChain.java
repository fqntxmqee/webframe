
package org.webframe.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-21 下午02:06:07
 */
public class WFFilterChain implements FilterChain {

	private final FilterChain		originalChain;

	private final List<Filter>		additionalFilters;

	private int							currentPosition	= 0;

	protected Log						log					= LogFactory.getLog(getClass());

	private WFFilterProxy	webframeFilterProxy;

	public WFFilterChain(FilterChain chain, List<Filter> additionalFilters) {
		this.originalChain = chain;
		this.additionalFilters = additionalFilters;
	}

	/* (non-Javadoc)
	* @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	*/
	public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException,
				ServletException {
		if (currentPosition == additionalFilters.size()) {
			// 当webframeFilterProxy为null时，说明当前FilterChain是lastChain
			if (webframeFilterProxy != null) {
				FilterChain lastChain = webframeFilterProxy.afterSecurityFilterChain(request, response, originalChain);
				if (webframeFilterProxy.hasSecurity) {
					webframeFilterProxy.superDoFilter(request, response, lastChain);
				} else {
					lastChain.doFilter(request, response);
				}
			} else {
				originalChain.doFilter(request, response);
			}
		} else {
			currentPosition++;
			Filter nextFilter = additionalFilters.get(currentPosition - 1);
			if (log.isDebugEnabled()) {
				log.debug("At position "
							+ currentPosition
							+ " of "
							+ additionalFilters.size()
							+ " in additional filter chain; firing Filter: '"
							+ nextFilter.getClass().getSimpleName()
							+ "'");
			}
			nextFilter.doFilter(request, response, this);
		}
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, WFFilterProxy webframeFilterProxy)
				throws IOException, ServletException {
		this.webframeFilterProxy = webframeFilterProxy;
		this.doFilter(request, response);
	}
}
