
package org.webframe.support.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.webframe.support.driver.resource.jar.JarResourceLoader;
import org.webframe.support.driver.resource.jar.JarResourcePatternResolver;

/**
 * ResourceUtils
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-4-27 下午1:50:44
 * @version
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {

	private static Log			log			= LogFactory.getLog(ResourceUtils.class);

	private static ClassLoader	classLoader	= ClassUtils.getDefaultClassLoader();

	private static PathMatcher	matcher		= new AntPathMatcher();

	/**
	 * 获取所有符合path文件的URL集合
	 * 
	 * @param path 文件路径
	 * @return 不返回null
	 * @throws IOException
	 * @author 黄国庆 2012-5-8 下午9:54:00
	 */
	public static List<URL> getUrls(String path) throws IOException {
		List<URL> urls = new ArrayList<URL>();
		if (path == null) {
			return urls;
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		Enumeration<URL> enumeration = classLoader.getResources(path);
		while (enumeration.hasMoreElements()) {
			urls.add(enumeration.nextElement());
		}
		return urls;
	}

	/**
	 * 根据指定JarURLConnection集合，获取符合pattern的Resource list集合
	 * 
	 * @param urls JarURLConnection集合
	 * @param pattern 符合AntPathMatcher规则的字符串
	 * @return 不为null
	 * @throws IOException
	 * @author 黄国庆 2012-5-8 下午9:57:11
	 */
	public static List<Resource> loadResources(List<JarURLConnection> urls, String pattern)
				throws IOException {
		List<Resource> resources = new ArrayList<Resource>();
		for (JarURLConnection jarURLConnection : urls) {
			JarResourcePatternResolver jrpr = JarResourcePatternResolver.getInstance(jarURLConnection);
			resources.addAll(Arrays.asList(jrpr.getResources(pattern)));
		}
		return resources;
	}

	public static Resource[] getSubResources(String rootPath, String path, boolean all) {
		if (path == null || "".equals(path)) {
			return new Resource[0];
		}
		if (!path.startsWith("/")) {
			log.warn("相对路径格式错误，没有以'/'开始：" + path);
			return new Resource[0];
		}
		String jarPath = path;
		String dir = jarPath;
		if (matcher.isPattern(dir)) {
			dir = StringUtils.getPatternRoot(dir);
		}
		if (rootPath == null) {
			List<Resource> jarSubs = getSubResourcesForJar(dir, jarPath);
			return jarSubs.toArray(new Resource[jarSubs.size()]);
		}
		rootPath = StringUtils.cleanPath(rootPath);
		List<Resource> fileSubs = getSubResourcesForFile(rootPath, dir, jarPath);
		if (!all) {
			return fileSubs.toArray(new Resource[fileSubs.size()]);
		}
		fileSubs.addAll(getSubResourcesForJar(dir, jarPath));
		return fileSubs.toArray(new Resource[fileSubs.size()]);
	}

	/**
	 * 获取指定path路径下的资源信息。如果rootPath为null，直接从jar中相对path中获取。
	 * 如果rootPath不为null：如果all为false，直接从rootPath对应的path中获取；
	 * 如果all为true，合并返回从jar包中相对path资源信息加上rootPath对应的path资源信息
	 * 
	 * @param rootPath 指定物理文件夹路径
	 * @param path 相当资源路径，例如：/images/**, /images/*.jpg
	 * @param all 是否合并获取资源
	 * @return 不为null的数组集合
	 * @author 黄国庆 2012-5-8 下午3:29:00
	 */
	public static String[] getSubs(String rootPath, String path, boolean all) {
		if (path == null || "".equals(path)) {
			return new String[0];
		}
		if (!path.startsWith("/")) {
			log.warn("相对路径格式错误，没有以'/'开始：" + path);
			return new String[0];
		}
		String jarPath = path.substring(1);
		String dir = jarPath;
		if (matcher.isPattern(jarPath)) {
			dir = StringUtils.getPatternRoot(jarPath);
		}
		if (rootPath == null) {
			Set<String> jarSubs = getSubsForJar(dir, jarPath);
			return jarSubs.toArray(new String[jarSubs.size()]);
		}
		if (!rootPath.endsWith("/")) {
			rootPath = rootPath + "/";
		}
		rootPath = StringUtils.cleanPath(rootPath);
		Set<String> fileSubs = getSubsForFile(rootPath, dir, jarPath);
		if (!all) {
			return fileSubs.toArray(new String[fileSubs.size()]);
		}
		fileSubs.addAll(getSubsForJar(dir, jarPath));
		return fileSubs.toArray(new String[fileSubs.size()]);
	}

	/**
	 * 从jar包中获取资源信息
	 * 
	 * @param dir 相对跟目录，去除匹配表达式的路径
	 * @param jarPath 包含匹配表达式的path路径
	 * @return 不为null
	 * @author 黄国庆 2012-5-8 下午3:35:36
	 */
	private static Set<String> getSubsForJar(String dir, String jarPath) {
		Set<String> subs = new HashSet<String>();
		try {
			Enumeration<URL> dirs = classLoader.getResources(dir);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				URLConnection urlConnection = url.openConnection();
				if (urlConnection instanceof JarURLConnection) {
					JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
					JarResourcePatternResolver patternResolver = JarResourcePatternResolver.getInstance(jarURLConnection);
					JarResourceLoader jarResourceLoader = patternResolver.getJarResourceLoader();
					subs.addAll(jarResourceLoader.getEntryFilesByDir(jarPath,
						matcher));
				}
			}
		} catch (IOException e) {
			// ignore
		}
		removeFolder(subs);
		return subs;
	}

	/**
	 * 从物理文件夹中获取资源信息
	 * 
	 * @param rootPath 指定物理文件夹路径
	 * @param dir 相对跟目录，去除匹配表达式的路径
	 * @param jarPath 包含匹配表达式的path路径
	 * @return
	 * @author 黄国庆 2012-5-8 下午3:37:33
	 */
	private static Set<String> getSubsForFile(String rootPath, String dir, String jarPath) {
		File file = new File(rootPath + dir);
		Set<String> subs = new HashSet<String>();
		if (file.exists() && file.isDirectory()) {
			boolean recursive = false;
			if (!dir.equals(jarPath)) {
				recursive = true;
			}
			Collection<?> files = FileUtils.listFiles(file, null, recursive);
			for (Object object : files) {
				if (!(object instanceof File)) {
					continue;
				}
				File child = (File) object;
				String absolutePath = StringUtils.cleanPath(child.getAbsolutePath());
				String relativePath = absolutePath.substring(rootPath.length());
				if (recursive) {
					if (matcher.match(jarPath, relativePath)) {
						subs.add(relativePath);
					}
				} else {
					subs.add(relativePath);
				}
			}
		}
		removeFolder(subs);
		return subs;
	}

	private static List<Resource> getSubResourcesForJar(String dir, String jarPath) {
		List<Resource> subs = new ArrayList<Resource>();
		try {
			if (dir.startsWith("/")) {
				dir = dir.substring(1);
			}
			Enumeration<URL> dirs = classLoader.getResources(dir);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				URLConnection urlConnection = url.openConnection();
				if (urlConnection instanceof JarURLConnection) {
					JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
					JarResourcePatternResolver patternResolver = JarResourcePatternResolver.getInstance(jarURLConnection);
					subs.addAll(Arrays.asList(patternResolver.getResources(jarPath)));
				}
			}
		} catch (IOException e) {
			// ignore
		}
		removeFolder(subs);
		return subs;
	}

	private static List<Resource> getSubResourcesForFile(String rootPath, String dir, String jarPath) {
		File file = new File(rootPath + dir);
		List<Resource> subs = new ArrayList<Resource>();
		if (file.exists() && file.isDirectory()) {
			boolean recursive = false;
			if (!dir.equals(jarPath)) {
				recursive = true;
			}
			Collection<?> files = FileUtils.listFiles(file, null, recursive);
			for (Object object : files) {
				if (!(object instanceof File)) {
					continue;
				}
				File child = (File) object;
				String absolutePath = StringUtils.cleanPath(child.getAbsolutePath());
				String relativePath = absolutePath.substring(rootPath.length());
				if (recursive) {
					if (matcher.match(jarPath, relativePath)) {
						subs.add(new FileSystemResource(child));
					}
				} else {
					subs.add(new FileSystemResource(child));
				}
			}
		}
		removeFolder(subs);
		return subs;
	}

	private static void removeFolder(Set<String> subs) {
		Iterator<String> it = subs.iterator();
		while (it.hasNext()) {
			String sub = it.next();
			if (sub.endsWith("/")) {
				it.remove();
			}
		}
	}

	private static void removeFolder(List<Resource> subs) {
		Iterator<Resource> it = subs.iterator();
		while (it.hasNext()) {
			Resource sub = it.next();
			if (sub.getFilename().endsWith("/")) {
				it.remove();
			}
		}
	}
}
