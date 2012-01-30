/*
 * wf-easymodule
 * Created on 2012-1-29-下午08:45:28
 */

package org.webframe.easy;

import org.webframe.support.driver.AbstractModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;

/**
 * 快速简易开发模块
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 下午08:45:28
 * @version
 */
public class EasyModulePluginDriver extends AbstractModulePluginDriver {

	static {
		ModulePluginManager.registerDriver(new EasyModulePluginDriver());
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.ModulePluginDriver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "EasyModule";
	}
}
