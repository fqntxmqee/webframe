
package org.webframe.web.spring;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.webframe.core.util.BeanUtils;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.driver.loader.DefaultModulePluginLoader;
import org.webframe.support.driver.loader.ModulePluginLoader;
import org.webframe.support.util.SystemLogUtils;
import org.webframe.web.util.WebSourcesUtils;

/**
 * 扩展spring {@link org.springframework.web.context.ContextLoaderListener} webframe框架web加载入口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:08:42
 * @version
 */
public class WFContextLoaderListener extends ContextLoaderListener {

	private String						MODULE_PLUGIN_DRIVER_LOADER	= "modulePluginLoaderName";

	private String						WEBFRAME_SYSTEM_LOG				= "webframeSystemLog";

	private String						defaultModulePluginLoaderName	= null;

	private String						webRealPath							= null;

	private Map<String, String>	parameters							= new HashMap<String, String>();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		webRealPath = servletContext.getRealPath("/");
		initParameters(servletContext);
		initModulePluginDriver(servletContext);
		SystemLogUtils.rootPrintln("完成模块驱动初始化！");
		initWebApplicationContext(servletContext);
		SystemLogUtils.rootPrintln("完成模块spring配置文件初始化！");
		// 初始化模块插件的web资源
		WebSourcesUtils.initWebSources(webRealPath);
		SystemLogUtils.rootPrintln("完成模块web资源初始化！");
	}
	
	protected void initParameters(ServletContext servletContext) {
		Enumeration<?> enumeration = servletContext.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
			Object parameterName = enumeration.nextElement();
			if (parameterName != null) {
				String name = parameterName.toString();
				parameters.put(name, servletContext.getInitParameter(name));
			}
		}
	}

	/**
	 * 初始化模块插件驱动
	 * 
	 * @param servletContext
	 * @author 黄国庆 2011-4-5 下午03:40:05
	 */
	protected void initModulePluginDriver(ServletContext servletContext) {
		ModulePluginLoader pluginLoader = initModulePluginLoader(servletContext);
		try {
			pluginLoader.loadModulePlugin();
		} catch (DriverNotExistException e) {
			SystemLogUtils.errorPrintln(e.getMessage());
		}
		ModulePluginUtils.cacheModulePluginConfig(webRealPath);
	}

	/**
	 * 初始化模块插件类驱动加载类
	 * 
	 * @param servletContext
	 * @return
	 * @author 黄国庆 2012-4-28 上午8:58:16
	 */
	protected ModulePluginLoader initModulePluginLoader(ServletContext servletContext) {
		defaultModulePluginLoaderName = parameters.get(MODULE_PLUGIN_DRIVER_LOADER);
		if (defaultModulePluginLoaderName == null) {
			defaultModulePluginLoaderName = DefaultModulePluginLoader.class.getName();
		}
		Class<?> driverClass = null;
		try {
			driverClass = ClassUtils.forName(defaultModulePluginLoaderName,
				ClassUtils.getDefaultClassLoader());
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
		BeanUtils.setBeanProperties(pluginLoader, parameters);
		if ("false".equals(parameters.get(WEBFRAME_SYSTEM_LOG))) {
			pluginLoader.enableWebframeLog(false);
		}
		return pluginLoader;
	}

	@Override
	protected Class<?> determineContextClass(ServletContext servletContext) {
		String contextClassName = parameters.get(CONTEXT_CLASS_PARAM);
		if (contextClassName == null) {
			contextClassName = WFApplicationContext.class.getName();
			try {
				return ClassUtils.forName(contextClassName,
					ClassUtils.getDefaultClassLoader());
			} catch (ClassNotFoundException ex) {
				throw new ApplicationContextException("Failed to load custom context class ["
							+ contextClassName
							+ "]", ex);
			}
		}
		return super.determineContextClass(servletContext);
	}
}
