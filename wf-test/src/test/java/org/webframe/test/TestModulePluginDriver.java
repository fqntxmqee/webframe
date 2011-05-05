/*
 * wf-test
 * Created on 2011-5-5-下午04:44:41
 */

package org.webframe.test;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午04:44:41
 */
public class TestModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new TestModulePluginDriver());
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	public String getModuleName() {
		return "TestModule";
	}

	@Override
	public String getSpringContextLocation() {
		return "/spring";
	}
}
