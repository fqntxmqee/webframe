
package org.webframe.support.driver.loader;

import org.webframe.support.driver.exception.DriverNotExistException;

/**
 * 默认模块插件驱动加载器
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:12:21
 */
public class DefaultModulePluginLoader extends AbstractModulePluginLoader {

	private String[]	drivers;

	public DefaultModulePluginLoader(String[] drivers) {
		this.drivers = drivers;
	}

	@Override
	public void loadModulePlugin() throws DriverNotExistException {
		loadModulePlugin(drivers);
	}
}
