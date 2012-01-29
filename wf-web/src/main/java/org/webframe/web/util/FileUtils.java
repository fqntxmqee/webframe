/*
 * wf-web
 * Created on 2011-5-9-下午09:03:21
 */

package org.webframe.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.webframe.support.util.SystemLogUtils;

/**
 * 文件工具类，提供单个文件的复制
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:27:23
 * @version
 */
public class FileUtils {

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @author 黄国庆 2011-5-9 下午09:05:03
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			copyFile(new FileInputStream(new File(oldPath)), new File(newPath));
		} catch (Exception e) {
			SystemLogUtils.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param inStream
	 * @param targetFile
	 * @author 黄国庆 2012-1-29 上午09:27:47
	 */
	public static void copyFile(InputStream inStream, File targetFile) {
		try {
			// 新文件目录不存在,则创建
			File folder = targetFile.getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			if (inStream != null) { // 文件存在时
				FileOutputStream fs = new FileOutputStream(targetFile);
				byte[] buffer = new byte[1444];
				int bytesum = 0, byteread = 0;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			} else {
				throw new Exception("找不到原文件!");
			}
		} catch (Exception e) {
			SystemLogUtils.println("复制单个文件操作出错!");
			e.printStackTrace();
		}
	}
}
