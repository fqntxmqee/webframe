
package org.webframe.support.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 提供class类文件与jar包的相关联系操作方法；
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2011-4-7 上午09:23:35
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

	private static final Log	log	= LogFactory.getLog(ClassUtils.class);

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
			log.error(e.getMessage(), e);
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

	/**
	 * 如果clazz在jar包中，返回null；否则返回clazz所在的classes的根目录
	 * 
	 * @param clazz
	 * @return
	 * @author 黄国庆 2012-2-7 下午12:58:15
	 */
	public static Resource getClassesRootResource(Class<?> clazz) {
		String classesRoot = "";
		try {
			Resource clazzResource = getResource(clazz);
			if (isInJar(clazzResource)) {
				return null;
			}
			String file = clazzResource.getURL().getFile();
			String clazzPath = convertClassNameToResourcePath(clazz.getName());
			String[] arr = file.split(clazzPath);
			classesRoot = arr[0];
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new FileSystemResource(classesRoot);
	}
	
	/**
	 * 根据class获取classes的绝对路径；如果class在jar包中，返回null
	 * 
	 * @param clazz
	 * @return
	 * @author 黄国庆 2012-6-5 下午9:17:16
	 */
	public static String getClassesRootPath(Class<?> clazz) {
		Resource resource = getClassesRootResource(clazz);
		if (resource != null && resource.exists()) {
			try {
				return ResourceUtils.getAbsolutePath(resource);
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}
}
