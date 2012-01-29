
package org.webframe.web;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * web模块插件驱动
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:36:26
 * @version
 */
public class WebModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new WebModulePluginDriver());
	}

	@Override
	public String getModuleName() {
		return "WebModule";
	}
}
