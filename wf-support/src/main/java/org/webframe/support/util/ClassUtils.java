
package org.webframe.support.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URLConnection;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-7 上午09:23:35
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

	/**
	 * 指定的class文件是否存在jar包中，true or false；捕获IOException异常，返回false。
	 * 
	 * @param clazz
	 * @return
	 * @author 黄国庆 2011-5-5 下午05:35:17
	 */
	public static boolean isInJar(Class<?> clazz) {
		return isInJar(getResource(clazz));
	}

	/**
	 * 指定Resource是否存在jar包中，true or false；捕获IOException异常，返回false。
	 * 
	 * @param resource
	 * @return
	 * @author 黄国庆 2011-5-5 下午08:40:43
	 */
	public static boolean isInJar(Resource resource) {
		try {
			URLConnection urlConnection = resource.getURL().openConnection();
			return urlConnection instanceof JarURLConnection;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取指定class文件Resource
	 * 
	 * @param clazz
	 * @return
	 * @author 黄国庆 2011-5-5 下午05:37:14
	 */
	public static Resource getResource(Class<?> clazz) {
		String resourcePath = ResourceLoader.CLASSPATH_URL_PREFIX
					+ convertClassNameToResourcePath(clazz.getName())
					+ CLASS_FILE_SUFFIX;
		return new DefaultResourceLoader().getResource(resourcePath);
	}
}
