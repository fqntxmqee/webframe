/*
 * wf-web-springmvc
 * Created on 2012-1-30-下午01:50:11
 */

package org.webframe.web.springmvc.view.jsp;

import java.io.File;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.web.servlet.view.JstlView;

/**
 * 先检查Jsp物理文件是否存在
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午01:50:11
 * @version
 */
public class JspCheckView extends JstlView {

	private String	jspRealPath	= "";

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return new File(jspRealPath).exists();
	}

	@Override
	protected void initServletContext(ServletContext servletContext) {
		super.initServletContext(servletContext);
		jspRealPath = servletContext.getRealPath(getUrl());
	}
}
