
package org.webframe.support.driver.resource.jar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * JarResource资源类，用于匹配搜索jar包中的资源文件
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-6 下午04:40:09
 */
public class JarResourcePatternResolver extends PathMatchingResourcePatternResolver {

	public JarResourcePatternResolver(Class<?> jarClass) throws IOException {
		this(new JarResourceLoader(jarClass));
	}

	public JarResourcePatternResolver(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		if (getResourceLoader() instanceof JarResourceLoader) {
			return findPathMatchingJarResources(locationPattern);
		} else {
			String rootDirPath = determineRootDir(locationPattern);
			Resource[] rootDirResources = super.getResources(rootDirPath);
			for (Resource rootDirResource : rootDirResources) {
				rootDirResource = resolveRootDirResource(rootDirResource);
			}
			return super.getResources(locationPattern);
		}
	}

	protected Resource[] findPathMatchingJarResources(String locationPattern) {
		List<Resource> result = new ArrayList<Resource>(16);
		JarResourceLoader jarResourceLoader = (JarResourceLoader) getResourceLoader();
		Set<String> entriesPath = jarResourceLoader.getEntriesPath();
		if (entriesPath == null) return result.toArray(new Resource[result.size()]);
		for (String entryPath : entriesPath) {
			String path = "/" + entryPath;
			if (getPathMatcher().match(locationPattern, path)) {
				Resource resource = getResource(path);
				if (resource == null) continue;
				result.add(resource);
			}
		}
		return result.toArray(new Resource[result.size()]);
	}

	public void close() throws IOException {
		if (getResourceLoader() instanceof JarResourceLoader) {
			((JarResourceLoader) getResourceLoader()).close();
		}
	}
}
