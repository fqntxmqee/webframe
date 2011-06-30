
package org.webframe.web;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * web模块插件驱动
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-18 下午06:55:55
 */
public class WebModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new WebModulePluginDriver());
	}

	@Override
	public String getSpringContextLocation() {
		return "/spring";
	}

	@Override
	public String getModuleName() {
		return "WebModule";
	}
}
