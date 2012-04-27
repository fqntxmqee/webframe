package org.webframe.support.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.springframework.core.io.Resource;
import org.webframe.support.driver.resource.jar.JarResourcePatternResolver;

/**
 * ResourceUtils
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-4-27 下午1:50:44
 * @version
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {

	public static List<URL> getUrls(String path) throws IOException {
		Enumeration<URL> enumeration = ClassUtils.getDefaultClassLoader()
			.getResources(path);
		List<URL> urls = new ArrayList<URL>();
		while (enumeration.hasMoreElements()) {
			urls.add(enumeration.nextElement());
		}
		return urls;
	}

	public static List<Resource> loadResources(List<JarURLConnection> urls, String pattern)
				throws IOException {
		List<Resource> resources = new ArrayList<Resource>();
		for (JarURLConnection jarURLConnection : urls) {
			JarResourcePatternResolver jrpr = new JarResourcePatternResolver(jarURLConnection);
			resources.addAll(Arrays.asList(jrpr.getResources(pattern)));
		}
		return resources;
	}
}
