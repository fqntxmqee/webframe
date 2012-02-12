
package org.webframe.support.driver.resource.jar;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.PathMatcher;
import org.webframe.support.util.ClassUtils;

/**
 * jar包中的资源文件加载器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-6 下午04:16:45
 */
public class JarResourceLoader extends DefaultResourceLoader {

	private JarResource			jarResource		= null;

	private JarURLConnection	jarCon			= null;

	private Set<String>			entriesPath		= null;

	private Class<?>				jarClass			= null;

	private boolean				isJarResource	= false;

	public JarResourceLoader(Class<?> jarClass) throws IOException {
		this.jarClass = jarClass;
		Resource jarClassResource = ClassUtils.getResource(jarClass);
		URLConnection urlConnection = jarClassResource.getURL().openConnection();
		if (urlConnection instanceof JarURLConnection) {
			jarCon = (JarURLConnection) urlConnection;
			jarCon.setUseCaches(false);
			jarResource = new JarResource(jarCon);
			entriesPath = jarResource.getEntriesPath();
			isJarResource = true;
		}
	}

	public Set<String> getEntriesPath() {
		return entriesPath;
	}

	public Set<String> getEntryFilesByDir(String directory, PathMatcher matcher) {
		return jarResource.getEntryFilesByDir(directory, matcher);
	}

	public JarURLConnection getJarURLConnection() {
		return jarCon;
	}

	@Override
	protected Resource getResourceByPath(String path) {
		if (hasJarResource()) {
			return jarResource.createRelative(path);
		} else {
			URL url = this.jarClass.getResource(path);
			if (url == null) {
				return new ClassPathResource(path, this.jarClass);
			}
			return new FileSystemResource(url.getFile());
		}
	}

	protected boolean hasJarResource() {
		return isJarResource;
	}

	public void close() throws IOException {
		if (jarResource != null) {
			jarResource.close();
		}
	}
}
