package org.webframe.support.util;

import org.junit.Test;


/**
 * 字符串工具类测试类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-10 下午2:47:15
 * @version
 */
public class StringUtilsTest {

	/**
	 * Test method for {@link org.webframe.support.util.StringUtils#getFileDirectory(java.lang.String)}.
	 */
	@Test
	public void testGetFileDirectory() {
		String[] strs = {
					"/org/webframe/**/*.class", "/org/webframe/**", "/org/webframe"};
		for (String string : strs) {
			System.out.println("testGetFileDirectory(): "
						+ string
						+ " <==> "
						+ StringUtils.getFileDirectory(string));
		}
	}

	/**
	 * Test method for {@link org.webframe.support.util.StringUtils#getPatternRoot(java.lang.String)}.
	 */
	@Test
	public void testGetPatternRoot() {
		String[] strs = {
					"/org/webframe/**/*.class", "/org/webframe/**", "/org/webframe"};
		for (String string : strs) {
			System.out.println("testGetPatternRoot(): "
						+ string
						+ " <==> "
						+ StringUtils.getPatternRoot(string));
		}
	}

	/**
	 * Test method for {@link org.webframe.support.util.StringUtils#getArtifactId(java.lang.String)}.
	 */
	@Test
	public void testGetArtifactId() {
		String[] strs = {
					"mysql-connector-java-5.1.12.jar", "junit-4.8.2.jar",
					"bi-basic-2.0.0-SNAPSHOT.jar", "spring-aop-3.1.0.RELEASE.jar",
					"jetty-server-7.5.4.v20111024.jar",
					"bi-jta-2.0.0-20120406.065642-2.jar"};
		for (String string : strs) {
			System.out.println("testGetArtifactId(): "
						+ string
						+ " <==> "
						+ StringUtils.getArtifactId(string));
		}
	}
}
