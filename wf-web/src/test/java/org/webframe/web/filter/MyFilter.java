/*
 * wf-web
 * Created on 2011-5-26-下午01:44:55
 */

package org.webframe.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-26 下午01:44:55
 */
public class MyFilter extends GenericFilterBean {

	private String	name	= "";

	private String	param	= "name";

	public String getName() {
		if (this.name == null) {
			this.setName(getFilterName());
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
				ServletException {
		SystemLogUtils.println("filter name: " + getName());
		String name = request.getParameter(getParam());
		response.getWriter().write(name);
	}
}
