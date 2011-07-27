
package org.webframe.core.module;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-21 下午10:43:21
 */
public class TestUserModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new TestUserModulePluginDriver());
	}

	@Override
	public String getEntityLocation() {
		return "/org/webframe/core/module";
	}

	@Override
	public String getModuleName() {
		return "TestUserModule";
	}
}
