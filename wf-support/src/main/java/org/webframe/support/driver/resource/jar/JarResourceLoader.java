
package org.webframe.support.driver.resource.jar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.PathMatcher;
import org.webframe.support.util.ClassUtils;
import org.webframe.support.util.StringUtils;

/**
 * jar包中的资源文件加载器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-6 下午04:16:45
 */
public class JarResourceLoader extends DefaultResourceLoader {

	private final Map<String, Set<String>>	entryFilesPathMap	= new HashMap<String, Set<String>>(16);

	private Set<String>							entriesPath			= new HashSet<String>(16);

	private JarResource							jarResource			= null;

	private JarURLConnection					jarCon				= null;

	private JarFile								jarFile				= null;

	private String									jarName				= null;

	private String									jarShorName			= null;

	private Class<?>								jarClass				= null;

	private boolean								isJarResource		= false;

	public JarResourceLoader(Class<?> jarClass) throws IOException {
		this.jarClass = jarClass;
		Resource jarClassResource = ClassUtils.getResource(jarClass);
		URLConnection urlConnection = jarClassResource.getURL().openConnection();
		if (urlConnection instanceof JarURLConnection) {
			init((JarURLConnection) urlConnection);
		}
	}

	public JarResourceLoader(JarURLConnection jarURLConnection)
				throws IOException {
		init(jarURLConnection);
	}

	public Set<String> getEntriesPath() {
		return entriesPath;
	}

	public Set<String> getEntryFilesByDir(String directory, PathMatcher matcher) {
		if (directory == null) {
			return null;
		}
		String pattern = resolveJarEntryPath(directory);
		if (!matcher.isPattern(pattern)) {
			return entryFilesPathMap.get(pattern);
		}
		String rootPattern = resolvePattern(pattern);
		Set<String> matches = new HashSet<String>();
		for (String key : entryFilesPathMap.keySet()) {
			if (matcher.match(rootPattern, key)) {
				Set<String> set = entryFilesPathMap.get(key);
				if (directory.endsWith("**")) {
					matches.addAll(set);
				} else {
					for (String string : set) {
						if (matcher.match(pattern, string)) {
							matches.add(string);
						}
					}
				}
			}
		}
		return matches;
	}

	public String getJarShortName() {
		return jarShorName;
	}

	public JarFile getJarFile() {
		return jarFile;
	}

	public Resource getResource() {
		return jarResource;
	}

	public long getLastModified() {
		if (hasJarResource()) {
			return jarCon.getLastModified();
		}
		return -1l;
	}

	public JarEntry getJarEntry(String entryName) {
		return jarFile.getJarEntry(entryName);
	}

	public boolean hasEntry(String path) {
		String entryName = resolveJarEntryPath(path);
		return getJarEntry(entryName) != null;
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

	public void close() throws IOException {
		if (jarFile != null) {
			jarFile.close();
		}
	}

	protected boolean hasJarResource() {
		return isJarResource;
	}

	protected String resolvePattern(String pattern) {
		// 如果包含'.'说明路径中找的是文件，截取到最后一个'/'
		if (pattern.indexOf(".") != -1) {
			return pattern.substring(0, pattern.lastIndexOf("/") + 1);
		}
		return pattern;
	}

	protected String resolveJarEntryPath(String path) {
		String delimiter = "/";
		String result = path;
		if (path.startsWith(delimiter)) {
			result = result.substring(1);
		}
		return result;
	}

	protected void init(JarURLConnection jarURLConnection) throws IOException {
		jarCon = jarURLConnection;
		jarFile = jarCon.getJarFile();
		jarName = "jar:" + jarCon.getJarFileURL().toExternalForm() + "!";
		String jarFileName = jarFile.getName();
		jarShorName = jarFileName.substring(jarFileName.lastIndexOf(File.separator) + 1);
		// jarCon.setUseCaches(false);
		for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			String key = name;
			if (!name.endsWith("/")) {
				key = StringUtils.getFileDirectory(name);
			}
			Set<String> pathSets = entryFilesPathMap.get(key);
			if (pathSets == null) {
				pathSets = new HashSet<String>();
				entryFilesPathMap.put(key, pathSets);
			}
			pathSets.add(name);
			entriesPath.add(name);
		}
		jarResource = new JarResource();
		isJarResource = true;
	}

	public class JarResource extends AbstractFileResolvingResource {

		private JarEntry	jarEntry		= null;

		private String		entryName	= null;

		private String		fullName		= null;
		
		private String		path			= null;

		protected JarResource() {
			this.entryName = jarCon.getEntryName();
			init(entryName);
		}

		private JarResource(String relativePath) {
			this.entryName = resolveJarEntryPath(relativePath);
			init(entryName);
		}

		public String getEntryName() {
			return this.entryName;
		}

		public JarEntry getEntry() {
			return this.jarEntry;
		}

		/**
		 * @param relativePath 路径，如果以'/'开头，则相对jar包中的资源；否则相对该资源文件
		 */
		@Override
		public Resource createRelative(String relativePath) {
			if (relativePath == null || "".equals(relativePath)) {
				throw new IllegalArgumentException("relativePath参数不能为空或null！");
			}
			if (!relativePath.startsWith("/")) {
				relativePath = this.path + relativePath;
			}
			return new JarResource(relativePath);
		}

		/* (non-Javadoc)
		 * @see org.springframework.core.io.Resource#getDescription()
		 */
		@Override
		public String getDescription() {
			StringBuilder builder = new StringBuilder("jar resource [");
			builder.append(fullName);
			builder.append(']');
			return builder.toString();
		}

		/* (non-Javadoc)
		 * @see org.springframework.core.io.InputStreamSource#getInputStream()
		 */
		@Override
		public InputStream getInputStream() throws IOException {
			if (exists()) {
				return jarFile.getInputStream(jarEntry);
			}
			throw new FileNotFoundException(getDescription()
						+ " cannot be opened because it does not exist");
		}

		@Override
		public URL getURL() throws IOException {
			if (exists()) {
				return new URL(getFilename());
			}
			throw new FileNotFoundException(getDescription()
						+ " cannot be opened because it does not exist");
		}

		@Override
		public boolean exists() {
			if (getEntry() == null) {
				return false;
			}
			return true;
		}

		@Override
		public String getFilename() {
			return fullName;
		}

		public String getJarName() {
			return getJarShortName();
		}

		@Override
		public int hashCode() {
			int code = fullName.hashCode();
			if (exists()) {
				code += jarEntry.hashCode();
			}
			return code;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof JarResource) {
				JarResource otherRes = (JarResource) obj;
				return (this.fullName.equals(otherRes.fullName) && this.entryName.equals(otherRes.entryName));
			}
			return false;
		}

		protected void init(String entryName) {
			int index = entryName.lastIndexOf("/");
			this.path = entryName.substring(0, index + 1);
			this.fullName = jarName + "/" + entryName;
			this.jarEntry = getJarEntry(entryName);
		}
	}
}
