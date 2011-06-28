
package org.webframe.web.springmvc.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * REST框架中，静态url统一跳转
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:33:48
 */
public class RestUrlRewriteFilter extends OncePerRequestFilter implements Filter {

	private String			prefix;

	private Set<String>	excludeExtentions	= null;

	private String[]		excludePrefixes	= new String[0];

	protected void initFilterBean() throws ServletException {
		try {
			initParameter(getFilterConfig());
		} catch (IOException e) {
			throw new ServletException("init paramerter error", e);
		}
	}

	private void initParameter(FilterConfig filterConfig) throws IOException {
		this.prefix = getStringParameter(filterConfig, "prefix", "/static");
		String excludeExtentionsString = getStringParameter(filterConfig, "excludeExtentions", "jsp, jspx, ftl");
		this.excludeExtentions = new HashSet<String>(Arrays.asList(excludeExtentionsString.split(",")));
		String excludePrefixsString = getStringParameter(filterConfig, "excludePrefixes", null);
		if (StringUtils.hasText(excludePrefixsString)) {
			this.excludePrefixes = excludePrefixsString.split(",");
		}
	}

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		String from = request.getRequestURI().substring(request.getContextPath().length());
		if (rewriteURL(from)) {
			String to = this.prefix + from;
			request.getRequestDispatcher(to).forward(request, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	private boolean rewriteURL(String from) {
		String extension = StringUtils.getFilenameExtension(from);
		if ((extension == null) || ("".equals(extension))) {
			return false;
		}
		for (String excludePrefix : this.excludePrefixes) {
			if (from.startsWith(excludePrefix)) {
				return false;
			}
		}
		return !this.excludeExtentions.contains(extension);
	}

	private String getStringParameter(FilterConfig filterConfig, String name, String defaultValue) {
		String value = filterConfig.getInitParameter(name);
		if ((value == null) || ("".equals(value.trim()))) {
			return defaultValue;
		}
		return value;
	}
}