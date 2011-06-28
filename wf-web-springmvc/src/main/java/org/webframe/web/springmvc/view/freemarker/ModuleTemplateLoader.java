
package org.webframe.web.springmvc.view.freemarker;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.webframe.support.driver.ModulePluginDriver;
import org.webframe.support.driver.ModulePluginManager;
import org.webframe.support.util.SystemLogUtils;

import freemarker.cache.URLTemplateLoader;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:48:12
 */
public class ModuleTemplateLoader extends URLTemplateLoader {

	private Map<String, URL>	urlMap	= new HashMap<String, URL>(16);

	/* (non-Javadoc)
	 * @see freemarker.cache.URLTemplateLoader#getURL(java.lang.String)
	 */
	@Override
	protected URL getURL(String name) {
		return getURLCache(name);
	}

	private URL getURLCache(String name) {
		if (urlMap.containsKey(name)) return urlMap.get(name);
		Enumeration<ModulePluginDriver> dirvers = ModulePluginManager.getDrivers();
		while (dirvers.hasMoreElements()) {
			ModulePluginDriver driver = dirvers.nextElement();
			Class<? extends ModulePluginDriver> loaderClass = driver.getClass();
			if (loaderClass == null) continue;
			String location = driver.getViewTempletLocation();
			if (location == null) continue;
			String path = canonicalizePrefix(location) + name;
			URL url = loaderClass.getResource(path);
			if (url == null) continue;
			urlMap.put(name, url);
			SystemLogUtils.println("Freemarker使用模块驱动：" + name + " --> " + driver.getClass().getName());
			return url;
		}
		return null;
	}
}
