
package org.webframe.support.driver.loader;

import org.webframe.support.driver.exception.DriverNotExistException;

/**
 * 模块插件加载器
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:11:24
 */
public interface ModulePluginLoader {

	public static final String	MODULE_PLUGIN_KEY	= "modulePlugin.drivers";

	/**
	 * 加载模块插件
	 * 
	 * @exception DriverNotExistException
	 * @author 黄国庆 2011-4-5 下午02:19:08
	 */
	void loadModulePlugin() throws DriverNotExistException;
}
