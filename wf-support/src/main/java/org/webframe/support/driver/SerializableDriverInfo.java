
package org.webframe.support.driver;

import java.io.Serializable;

/**
 * 可序列号的模块插件驱动信息
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-1 下午01:21:53
 */
class SerializableDriverInfo implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3332286880025759652L;

	String							driverClass;

	String							driverClassName;

	boolean							inJar					= false;

	long								lastModifyTime		= 0;

	String							jarName;

	SerializableDriverInfo(String driverClass, String driverClassName, boolean inJar, long lastModifyTime, String jarName) {
		this.driverClass = driverClass;
		this.driverClassName = driverClassName;
		this.inJar = inJar;
		this.lastModifyTime = lastModifyTime;
		this.jarName = jarName;
	}
}
