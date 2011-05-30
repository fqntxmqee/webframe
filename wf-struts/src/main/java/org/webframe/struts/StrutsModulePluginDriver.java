/*
 * wf-struts
 * Created on 2011-5-30-上午10:29:27
 */

package org.webframe.struts;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-30 上午10:29:27
 */
public class StrutsModulePluginDriver extends AbstractModulePluginDriver implements IStrutsSupport {

	static {
		ModulePluginManager.registerDriver(new StrutsModulePluginDriver());
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "StrutsModule";
	}

	@Override
	public String getStrutsConfigLocation() {
		return "/struts";
	}
}
