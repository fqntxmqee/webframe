
package org.webframe.support.driver.loader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.webframe.support.driver.exception.DriverNotExistException;

/**
 * Properties模块插件驱动加载器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:39:10
 */
public class PropertiesModulePluginLoader extends AbstractModulePluginLoader {

	/**
	 * 模块插件驱动properties文件路径
	 */
	private String	modulePluginProperties;

	public PropertiesModulePluginLoader() {
		this("modulePlugin.properties");
	}

	public PropertiesModulePluginLoader(String modulePluginProperties) {
		setModulePluginProperties(modulePluginProperties);
	}

	protected final String getModulePluginProperties() {
		return modulePluginProperties;
	}

	public void setModulePluginProperties(String modulePluginProperties) {
		this.modulePluginProperties = modulePluginProperties;
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.loader.ModulePluginLoader#loadModulePlugin()
	 */
	@Override
	public void loadModulePlugin() throws DriverNotExistException {
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties(modulePluginProperties);
			Resource res = new ClassPathResource(modulePluginProperties);
			if (res.exists()) {
				Properties override = PropertiesLoaderUtils.loadProperties(res);
				properties.putAll(override);
			}
			Set<Object> driverSet = new HashSet<Object>();
			for (Object key : properties.keySet()) {
				if ("1".equals(properties.get(key))) {
					driverSet.add(key);
				}
			}
			String[] drivers = driverSet.toArray(new String[driverSet.size()]);
			loadModulePlugin(drivers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
