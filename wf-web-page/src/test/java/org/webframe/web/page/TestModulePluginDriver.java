/*
 * wf-web
 * Created on 2011-5-26-下午02:02:18
 */

package org.webframe.web.page;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-26 下午02:02:18
 */
public class TestModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new TestModulePluginDriver());
	}

	@Override
	public String getSpringContextLocation() {
		return null;
	}

	@Override
	public String getEntityLocation() {
		return "/org/webframe/web/page/test";
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "TestModule";
	}
}
