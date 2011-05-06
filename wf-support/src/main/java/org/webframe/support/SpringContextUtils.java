
package org.webframe.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.webframe.support.driver.ModulePluginDriver;
import org.webframe.support.driver.ModulePluginDriverInfo;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-6 上午09:59:29
 */
public class SpringContextUtils extends ModulePluginUtils {

	private static final String	RESOURCE_PATTERN_SPRING	= "/**/wf-*.xml";

	/**
	 * @return not null
	 * @author 黄国庆 2011-4-6 上午10:14:45
	 */
	public static Resource[] getSpringContextResources() {
		List<Resource> resourcesList = new ArrayList<Resource>(16);
		Enumeration<ModulePluginDriverInfo> dirverInfos = getDriverInfos();
		SystemLogUtils.rootPrintln("Spring Context 配置文件Find Begin！");
		while (dirverInfos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = dirverInfos.nextElement();
			SystemLogUtils.secondPrintln(driverInfo.getDriver() + "Spring Context 配置文件Find！");
			List<Resource> list = getSpringContextResources(driverInfo);
			resourcesList.addAll(list);
			SystemLogUtils.thirdPrintln(driverInfo.getDriver() + "找到(" + list.size() + ")个配置文件！");
		}
		SystemLogUtils.rootPrintln("Spring Context 配置文件 Find End！");
		return resourcesList.toArray(new Resource[resourcesList.size()]);
	}

	/**
	 * 获取spring配置文件
	 * 
	 * @param driver 模块插件驱动
	 * @return not null
	 * @author 黄国庆 2011-4-6 上午09:45:18
	 */
	public static List<Resource> getSpringContextResources(ModulePluginDriverInfo driverInfo) {
		ModulePluginDriver driver = driverInfo.getDriver();
		List<Resource> springSources = new ArrayList<Resource>(16);
		Class<? extends ModulePluginDriver> loaderClass = driver.getClass();
		String springContext = driver.getSpringContextLocation();
		if (springContext == null) return springSources;
		String[] springContexts = StringUtils.tokenizeToStringArray(springContext, DELIMITERS);
		for (String location : springContexts) {
			String path = resolvePath(loaderClass, location);
			path = ClassUtils.convertClassNameToResourcePath(path);
			Resource[] resources = getResources(driverInfo, path + RESOURCE_PATTERN_SPRING);
			if (resources == null) continue;
			springSources.addAll(Arrays.asList(resources));
		}
		return springSources;
	}
}
