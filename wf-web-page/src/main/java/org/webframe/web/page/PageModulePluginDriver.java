/*
 * wf-web-page
 * Created on 2011-6-30-下午03:08:17
 */

package org.webframe.web.page;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * 分页模块插件驱动
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-30 下午03:08:17
 */
public class PageModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new PageModulePluginDriver());
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "PageModule";
	}

	@Override
	public String getSpringContextLocation() {
		return "/valuelist";
	}
}
