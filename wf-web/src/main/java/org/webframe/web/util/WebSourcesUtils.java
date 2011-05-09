/*
 * wf-web
 * Created on 2011-5-9-下午08:53:49
 */

package org.webframe.web.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.webframe.support.driver.ModulePluginDriver;
import org.webframe.support.driver.ModulePluginDriverInfo;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.support.util.ClassUtils;
import org.webframe.support.util.StringUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * 初始化模块插件包中的web资源，如果模块插件包中的资源是最新的，则不需要更新
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-6 上午09:49:03
 */
public class WebSourcesUtils extends ModulePluginUtils {

	private static String			defaultWebRealPath	= null;

	private static final String	RESOURCE_PATTERN_ALL	= "/**/*.*";

	/**
	 * 初始化模块插件包中的web资源，如果模块插件包中的资源是最新的，则不需要更新
	 * 
	 * @param webRealPath web容器的绝对路径
	 * @author 黄国庆 2011-4-6 上午09:56:07
	 */
	public static void initWebSources(String webRealPath) {
		if (defaultWebRealPath == null) defaultWebRealPath = webRealPath;
		SystemLogUtils.rootPrintln("Web资源初始化开始！");
		for (ModulePluginDriverInfo driverInfo : getNeedUpdateDriverInfos()) {
			List<Resource> resources = getWebSourcesResources(driverInfo);
			if (resources == null || defaultWebRealPath == null) continue;
			SystemLogUtils.secondPrintln(driverInfo.getDriver() + "Web资源初始化！");
			for (Resource resource : resources) {
				if (resource instanceof ClassPathResource || resource instanceof FileSystemResource) {
					String path;
					if (resource instanceof ClassPathResource) {
						ClassPathResource cpr = (ClassPathResource) resource;
						path = cpr.getPath();
					} else {
						FileSystemResource cpr = (FileSystemResource) resource;
						path = cpr.getPath();
						int index = path.lastIndexOf("classes");
						if (index <= 0) continue;
						path = path.substring(index + 7);
					}
					resolveResource(path, driverInfo, resource);
				}
			}
		}
		SystemLogUtils.rootPrintln("Web资源初始化结束！");
	}

	private static void resolveResource(String path, ModulePluginDriverInfo driverInfo, Resource resource) {
		try {
			AbstractResource cpr = (AbstractResource) resource;
			long sourceModified = cpr.lastModified();
			File targetFile = new File(defaultWebRealPath + path);
			if (!targetFile.exists()) {
				FileUtils.copyFile(resource.getInputStream(), targetFile);
				targetFile.setLastModified(sourceModified);
				SystemLogUtils.thirdPrintln(driverInfo.getDriver() + "Web资源：" + path + "复制！");
			} else {
				long targetModified = targetFile.lastModified();
				if (sourceModified != targetModified) {
					FileUtils.copyFile(resource.getInputStream(), targetFile);
					targetFile.setLastModified(sourceModified);
					SystemLogUtils.thirdPrintln(driverInfo.getDriver() + "Web资源：" + path + "更新！");
				}
			}
		} catch (IOException e) {
			SystemLogUtils.println(e.getMessage());
		}
	}

	/**
	 * 获取所有web资源
	 * 
	 * @param driver 模块插件驱动
	 * @return not null
	 * @author 黄国庆 2011-4-5 下午10:14:03
	 */
	public static List<Resource> getWebSourcesResources(ModulePluginDriverInfo driverInfo) {
		List<Resource> webSources = new ArrayList<Resource>(16);
		ModulePluginDriver driver = driverInfo.getDriver();
		Class<? extends ModulePluginDriver> loaderClass = driver.getClass();
		String webSourcesLocation = driver.getWebSourcesLocation();
		if (webSourcesLocation == null) return webSources;
		String[] webSourcesLocations = StringUtils.tokenizeToStringArray(webSourcesLocation, DELIMITERS);
		for (String location : webSourcesLocations) {
			String path = resolvePath(loaderClass, location);
			path = ClassUtils.convertClassNameToResourcePath(path);
			Resource[] resources = getResources(driverInfo, path + RESOURCE_PATTERN_ALL);
			if (resources == null) continue;
			webSources.addAll(Arrays.asList(resources));
		}
		return webSources;
	}
}