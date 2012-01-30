/*
 * wf-web-springmvc
 * Created on 2011-6-28-下午08:36:07
 */

package org.webframe.web.springmvc;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:36:07
 */
public class SpringMVCModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new SpringMVCModulePluginDriver());
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "SpringMVCModule";
	}

	@Override
	public String getViewTempletLocation() {
		return "view";
	}
}
