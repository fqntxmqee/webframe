/*
 * wf-struts
 * Created on 2011-5-30-上午10:32:52
 */

package org.webframe.struts.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.webframe.struts.IStrutsSupport;
import org.webframe.support.driver.ModulePluginDriver;
import org.webframe.support.driver.ModulePluginDriverInfo;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-30 上午10:32:52
 */
public class StrutsConfigUtils extends ModulePluginUtils {

	private static final String	RESOURCE_PATTERN_STRUTS	= "/**/struts-config*.xml";

	public static Resource[] getStrutsConfigResources() {
		List<Resource> resourcesList = new ArrayList<Resource>(16);
		Enumeration<ModulePluginDriverInfo> dirverInfos = getDriverInfos();
		SystemLogUtils.rootPrintln("Struts Config 配置文件Find Begin！");
		while (dirverInfos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = dirverInfos.nextElement();
			if (!(driverInfo.getDriver() instanceof IStrutsSupport)) continue;
			SystemLogUtils.secondPrintln(driverInfo.getDriver() + "Struts Config 配置文件Find！");
			List<Resource> list = getStrutsConfigResources(driverInfo);
			resourcesList.addAll(list);
			SystemLogUtils.thirdPrintln(driverInfo.getDriver() + "找到(" + list.size() + ")个配置文件！");
		}
		SystemLogUtils.rootPrintln("Struts Config 配置文件 Find End！");
		return resourcesList.toArray(new Resource[resourcesList.size()]);
	}

	public static List<Resource> getStrutsConfigResources(ModulePluginDriverInfo driverInfo) {
		List<Resource> strutsResources = new ArrayList<Resource>(16);
		ModulePluginDriver driver = driverInfo.getDriver();
		if (!(driver instanceof IStrutsSupport)) return strutsResources;
		IStrutsSupport strutsSupport = (IStrutsSupport) driver;
		Class<? extends ModulePluginDriver> loaderClass = driver.getClass();
		String strutsConfig = strutsSupport.getStrutsConfigLocation();
		if (strutsConfig == null) return strutsResources;
		String[] strutsConfigs = StringUtils.tokenizeToStringArray(strutsConfig, DELIMITERS);
		for (String config : strutsConfigs) {
			String path = resolvePath(loaderClass, config);
			path = ClassUtils.convertClassNameToResourcePath(path);
			Resource[] resources = getResources(driverInfo, path + RESOURCE_PATTERN_STRUTS);
			if (resources == null) continue;
			strutsResources.addAll(Arrays.asList(resources));
		}
		return strutsResources;
	}
}
