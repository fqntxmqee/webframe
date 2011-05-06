
package org.webframe.support.driver.loader;

import org.webframe.support.driver.exception.DriverNotExistException;

/**
 * 抽象模块插件加载器，提供class.forName加载模块插件类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:21:01
 */
public abstract class AbstractModulePluginLoader implements ModulePluginLoader {

	/**
	 * 通过给定的模块插件驱动类全路径数组，加载模块插件驱动
	 * 
	 * @param drivers 模块插件驱动类全路径数组，例如： <pre> String[] drivers = {
	 *           "com.berheley.wf.base.BaseModulePluginDriver",
	 *           "com.berheley.wf.module.CommonModulePluginDriver"}; </pre>
	 * @exception DriverNotExistException
	 * @author 黄国庆 2011-4-5 下午02:24:34
	 */
	protected void loadModulePlugin(String[] drivers) throws DriverNotExistException {
		if (drivers == null) throw new DriverNotExistException();
		String driverNotExists = "";
		for (String driver : drivers) {
			try {
				if (driver == null) throw new DriverNotExistException();
				Class.forName(driver, true, this.getClass().getClassLoader());
			} catch (ClassNotFoundException e) {
				driverNotExists += driver + "|";
			} catch (DriverNotExistException e) {
				driverNotExists += "null|";
			}
		}
		if (driverNotExists.length() > 0) {
			driverNotExists = driverNotExists.substring(0, driverNotExists.lastIndexOf("|"));
			throw new DriverNotExistException(driverNotExists);
		}
	}
}
