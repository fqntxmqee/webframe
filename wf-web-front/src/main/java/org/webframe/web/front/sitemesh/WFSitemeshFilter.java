/*
 * wf-web-front
 * Created on 2011-7-25-下午03:47:42
 */

package org.webframe.web.front.sitemesh;

import javax.servlet.FilterConfig;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.filter.PageFilter;

/**
 * 初始化sitemesh Factory
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-25 下午03:47:42
 */
public class WFSitemeshFilter extends PageFilter {

	protected static final String	SITEMESH_FACTORY	= "sitemesh.factory";

	@Override
	public void init(FilterConfig filterConfig) {
		Config config = new Config(filterConfig);
		Factory factory = new WFSitemeshFactory(config);
		config.getServletContext().setAttribute(SITEMESH_FACTORY, factory);
		super.init(filterConfig);
	}
}
