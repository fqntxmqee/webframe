
package org.webframe.support.driver.resource.jar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.Resource;
import org.webframe.support.util.StringUtils;

/**
 * JarResource资源类，用于处理jar包中的资源文件
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-6 下午08:27:52
 */
public class JarResource extends AbstractFileResolvingResource {

	private String							jarName				= null;

	private JarURLConnection			jarURLConnection;

	private JarEntry						jarEntry				= null;

	private String							path					= null;

	private Set<String>					entriesPath			= null;

	private Map<String, Set<String>>	entryFilesPathMap	= new HashMap<String, Set<String>>(16);

	private JarFile						jarFile				= null;

	public JarResource(JarURLConnection jarURLConnection) throws IOException {
		if (jarURLConnection == null) {
			throw new IllegalArgumentException("JarURLConnection must not be null");
		}
		this.jarURLConnection = jarURLConnection;
		this.jarFile = jarURLConnection.getJarFile();
		this.jarName = "jar:" + jarURLConnection.getJarFileURL().toExternalForm() + "!";
		entriesPath = new HashSet<String>(16);
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
	}

	private JarResource(JarURLConnection jarURLConnection,
				JarFile jarFile,
				String jarName,
				Set<String> entriesPath,
				String path) {
		this.jarURLConnection = jarURLConnection;
		this.jarFile = jarFile;
		this.entriesPath = entriesPath;
		this.jarEntry = getJarEntry(path);
		if (jarEntry != null) {
			this.jarName = jarName + StringUtils.replace(path, StringUtils.getSeparator(), "/");
		}
	}

	@Override
	public Resource createRelative(String relativePath) {
		return new JarResource(jarURLConnection, jarFile, jarName, entriesPath, relativePath);
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.io.Resource#getDescription()
	 */
	@Override
	public String getDescription() {
		StringBuilder builder = new StringBuilder("jar resource [");
		builder.append(jarName);
		builder.append(']');
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.io.InputStreamSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		if (jarEntry == null && path == null) {
			return jarURLConnection.getInputStream();
		} else if (jarEntry != null) {
			return jarFile.getInputStream(jarEntry);
		}
		throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
	}

	@Override
	public URL getURL() throws IOException {
		if (jarEntry != null) {
			return new URL(getFilename());
		}
		return jarURLConnection.getURL();
	}

	@Override
	public boolean exists() {
		if (jarEntry == null) {
			return false;
		}
		return true;
	}

	@Override
	public String getFilename() {
		return jarName;
	}

	@Override
	public int hashCode() {
		int code = this.jarName.hashCode();
		if (jarEntry != null) {
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
			return (this.jarName.equals(otherRes.jarName) && this.jarURLConnection.getContentLength() == otherRes.jarURLConnection.getContentLength());
		}
		return false;
	}

	public Set<String> getEntryFilesByDir(String directory) {
		if (directory == null) return null;
		return entryFilesPathMap.get(resolveJarEntryPath(directory));
	}

	public JarEntry getJarEntry(String entryName) {
		return jarFile.getJarEntry(resolveJarEntryPath(entryName));
	}

	public Set<String> getEntriesPath() {
		return entriesPath;
	}

	private String resolveJarEntryPath(String path) {
		String delimiter = "/";
		String result = path;
		if (path.startsWith(delimiter)) {
			result = result.substring(1);
		}
		return result;
	}

	public void close() throws IOException {
		jarFile.close();
	}
}
