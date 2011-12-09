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
		if (filePath == null) return null;
		int index = filePath.lastIndexOf("/");
		return filePath.substring(0, index + 1);
	}
}
