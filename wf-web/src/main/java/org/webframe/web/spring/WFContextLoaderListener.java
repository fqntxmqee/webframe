
package org.webframe.web.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.driver.loader.DefaultModulePluginLoader;
import org.webframe.support.driver.loader.ModulePluginLoader;
import org.webframe.support.util.SystemLogUtils;
import org.webframe.web.util.WebSourcesUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:53:00
 */
public class WFContextLoaderListener extends ContextLoaderListener {

	private String	MODULE_PLUGIN_DRIVER_LOADER	= "modulePluginLoaderName";

	private String	defaultModulePluginLoaderName	= null;

	private String	webRealPath							= null;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		webRealPath = servletContext.getRealPath("/");
		initModulePluginDriver(servletContext);
		initWebApplicationContext(servletContext);
	}

	/**
	 * 初始化模块插件驱动
	 * 
	 * @param servletContext
	 * @author 黄国庆 2011-4-5 下午03:40:05
	 */
	protected void initModulePluginDriver(ServletContext servletContext) {
		defaultModulePluginLoaderName = servletContext.getInitParameter(MODULE_PLUGIN_DRIVER_LOADER);
		if (defaultModulePluginLoaderName == null) {
			defaultModulePluginLoaderName = DefaultModulePluginLoader.class.getName();
		}
		Class<?> driverClass = null;
		try {
			driverClass = ClassUtils.forName(defaultModulePluginLoaderName, ClassUtils.getDefaultClassLoader());
		} catch (ClassNotFoundException ex) {
			SystemLogUtils.errorPrintln(ex.getMessage());
			driverClass = DefaultModulePluginLoader.class;
		}
		ModulePluginLoader pluginLoader = null;
		try {
			pluginLoader = (ModulePluginLoader) BeanUtils.instantiateClass(driverClass);
		} catch (BeanInstantiationException e) {
			// 默认模块插件加载器
			pluginLoader = new DefaultModulePluginLoader(new String[]{});
		}
		try {
			pluginLoader.loadModulePlugin();
		} catch (DriverNotExistException e) {
			SystemLogUtils.errorPrintln(e.getMessage());
		}
		ModulePluginUtils.cacheModulePluginConfig(webRealPath);
		// 初始化模块插件的web资源
		WebSourcesUtils.initWebSources(webRealPath);
	}

	@Override
	protected Class<?> determineContextClass(ServletContext servletContext) {
		String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);
		if (contextClassName == null) {
			contextClassName = WFApplicationContext.class.getName();
			try {
				return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
			} catch (ClassNotFoundException ex) {
				throw new ApplicationContextException("Failed to load custom context class [" + contextClassName + "]", ex);
			}
		}
		return super.determineContextClass(servletContext);
	}
}
