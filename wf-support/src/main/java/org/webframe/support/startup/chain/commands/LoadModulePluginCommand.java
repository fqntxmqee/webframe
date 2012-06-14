
package org.webframe.support.startup.chain.commands;

import org.apache.commons.chain.Context;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.driver.loader.DefaultModulePluginLoader;
import org.webframe.support.driver.loader.ModulePluginLoader;
import org.webframe.support.util.SystemLogUtils;

/**
 * 加载模块插件命令
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-7 上午9:09:14
 * @version
 */
public class LoadModulePluginCommand extends AbstractStopWatchCommand {

	/**
	 * 模块插件加载类名称
	 */
	public static final String	MODULE_PLUGIN_DRIVER_LOADER	= "module_plugin_loader";

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Command#execute(org.apache.commons.chain.Context)
	 */
	@Override
	public boolean executeWithStopWatch(Context context) throws Exception {
		Class<?> pluginLoaderClass = (Class<?>) context.get(MODULE_PLUGIN_DRIVER_LOADER);
		if (pluginLoaderClass == null) {
			return true;
		}
		loadModulePluginLoader(pluginLoaderClass);
		return false;
	}

	@Override
	protected String getStopWatchId() {
		return "LoadModulePlugin";
	}

	protected ModulePluginLoader initModulePluginLoader(Class<?> driverClass) {
		ModulePluginLoader pluginLoader = null;
		try {
			pluginLoader = (ModulePluginLoader) BeanUtils.instantiateClass(driverClass);
		} catch (BeanInstantiationException e) {
			// 默认模块插件加载器
			pluginLoader = new DefaultModulePluginLoader(new String[]{});
		}
		return pluginLoader;
	}

	protected void loadModulePluginLoader(Class<?> driverClass) {
		ModulePluginLoader pluginLoader = initModulePluginLoader(driverClass);
		try {
			pluginLoader.loadModulePlugin();
		} catch (DriverNotExistException e) {
			SystemLogUtils.errorPrintln(e.getMessage());
		}
	}
}
