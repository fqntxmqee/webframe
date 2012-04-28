package org.webframe.support.driver.relation;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.webframe.support.util.ResourceUtils;


/**
 *
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-4-27 下午2:59:18
 * @version 
 */
public class ModulePluginDependencyUtilTest {

	/**
	 * Test method for {@link org.webframe.support.driver.relation.ModulePluginDependencyUtil#sort(java.util.List)}.
	 */
	@Test
	public void testSort() {
	}

	/**
	 * Test method for
	 * {@link org.webframe.support.driver.relation.ModulePluginDependencyUtil#analyzeDependendy(java.util.List)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAnalyzeDependendy() throws Exception {
		List<JarURLConnection> urls = new ArrayList<JarURLConnection>();
		List<URL> lists = ResourceUtils.getUrls("org/apache/commons/io/IOUtils.class");
		for (URL url : lists) {
			URLConnection urlConnection = url.openConnection();
			if (urlConnection instanceof JarURLConnection) {
				urls.add((JarURLConnection) urlConnection);
			}
		}
		ModulePluginDependencyUtil.analyzeDependendy(urls, null);
	}
}
