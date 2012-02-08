/*
 * wf-web-front
 * Created on 2011-7-1-下午09:15:38
 */

package org.webframe.web.front;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-1 下午09:15:38
 */
public class FrontModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new FrontModulePluginDriver());
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "FrontModule";
	}

	@Override
	public String getWebSourcesLocation() {
		return "/WEB-INF";
	}

	@Override
	public String getEntityLocation() {
		return null;
	}

	@Override
	public String getViewTempletLocation() {
		return "view";
	}
}
