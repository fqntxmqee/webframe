/*
 * wf-support
 * Created on 2011-5-5-下午10:10:17
 */

package org.webframe.support.util;

import java.io.File;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午10:10:17
 */
public class StringUtils extends org.springframework.util.StringUtils {

	public static String getSeparator() {
		return File.separator;
	}

	public static String getFileDirectory(String filePath) {
		if (filePath == null) {
			return null;
		}
		int index = filePath.lastIndexOf("/");
		return filePath.substring(0, index + 1);
	}

	/**
	 * 获取匹配符的不包含**的根路径
	 * 
	 * @param pattern 可能包含**的匹配符
	 * @return
	 * @author 黄国庆 2012-5-10 下午2:45:36
	 */
	public static String getPatternRoot(String pattern) {
		if (pattern == null) {
			return null;
		}
		int doubleAsterisk = pattern.indexOf("**");
		if (doubleAsterisk != -1) {
			return pattern.substring(0, doubleAsterisk);
		}
		return getFileDirectory(pattern);
	}

	/**
	 * 通过jar文件名称，获取artifactId
	 * 
	 * @param jarShotName jar文件名称
	 * @return
	 * @author 黄国庆 2012-5-10 下午2:44:39
	 */
	public static String getArtifactId(String jarShotName) {
		if (null == jarShotName || "".equals(jarShotName)) {
			return jarShotName;
		}
		String regex = "(-\\d+)(\\.\\d+)*(-SNAPSHOT)?(\\.\\S+)?$";
		return jarShotName.replaceAll(regex, "");
	}
}
