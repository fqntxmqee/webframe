
package org.webframe.core;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * 默认基础模块插件驱动
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-18 下午06:55:55
 */
public class CoreModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new CoreModulePluginDriver());
	}

	@Override
	public String getEntityLocation() {
		return "model";
	}

	@Override
	public String getModuleName() {
		return "CoreModule";
	}
}
