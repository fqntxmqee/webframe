
package org.webframe.support.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-8 下午9:40:07
 * @version
 */
public class ResourceUtilsTest {

	/**
	 * Test method for {@link org.webframe.support.util.ResourceUtils#getUrls(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetUrls() throws IOException {
		List<URL> lists = ResourceUtils.getUrls("org/webframe/support/util/ResourceUtils.class");
		Assert.assertEquals("获取URL集合错误！", 1, lists.size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.support.util.ResourceUtils#loadResources(java.util.List, java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadResources() throws IOException {
		List<JarURLConnection> urls = new ArrayList<JarURLConnection>();
		List<URL> lists = ResourceUtils.getUrls("overview.html");
		for (URL url : lists) {
			URLConnection urlConnection = url.openConnection();
			if (urlConnection instanceof JarURLConnection) {
				urls.add((JarURLConnection) urlConnection);
			}
		}
		List<Resource> resources = ResourceUtils.loadResources(urls,
			"/META-INF/**");
		for (Resource resource : resources) {
			System.out.println("ResourceUtils#loadResources: " + resource);
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.support.util.ResourceUtils#getSubResources(java.lang.String, java.lang.String, boolean)}
	 * .
	 */
	@Test
	public void testGetSubResources() throws IOException {
		String rootPath = ResourceUtils.getAbsolutePath(ClassUtils.getClassesRootResource(ResourceUtils.class));
		String path = "/org/webframe/support/driver/resource/**";
		Resource[] resources = ResourceUtils.getSubResources(rootPath, path, true);
		Assert.assertEquals("获取资源文件错误！", resources.length,
			ResourceUtils.getSubs(rootPath, path, true).length);
		System.out.println(path + " <--> resource(size):" + resources.length);
		for (Resource resource : resources) {
			System.out.println("resource:" + resource);
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.support.util.ResourceUtils#getSubs(java.lang.String, java.lang.String, boolean)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetSubs() throws IOException {
		String rootPath = ResourceUtils.getAbsolutePath(ClassUtils.getClassesRootResource(ResourceUtils.class))
					+ "/";
		String path = "/org/webframe/support/driver/resource/**/JarResource*.class";
		String[] subs = ResourceUtils.getSubs(rootPath, path, true);
		Assert.assertEquals("获取资源文件错误！", subs.length,
			ResourceUtils.getSubResources(rootPath, path, true).length);
		System.out.println(path + " <--> string(size):" + subs.length);
		for (String sub : subs) {
			System.out.println("string:" + sub);
		}
	}
}
