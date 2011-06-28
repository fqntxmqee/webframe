
package org.webframe.web.springmvc.view.jstl;

import java.io.File;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.web.servlet.view.JstlView;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:48:46
 */
public class ModuleJstlView extends JstlView {

	private String	realPath	= "";

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return new File(realPath).exists();
	}

	@Override
	protected void initServletContext(ServletContext servletContext) {
		super.initServletContext(servletContext);
		realPath = servletContext.getRealPath(getUrl());
	}
}
