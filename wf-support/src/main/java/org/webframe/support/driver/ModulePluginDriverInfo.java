
package org.webframe.support.driver;

import org.webframe.support.driver.resource.jar.JarResourceLoader;

/**
 * 模块插件驱动详细信息
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-1 上午10:19:04
 */
public class ModulePluginDriverInfo {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7164720425115665612L;

	ModulePluginDriver			driver;

	Class<ModulePluginDriver>	driverClass;

	String							driverClassName;

	boolean							inJar					= false;

	long								lastModifyTime		= 0;

	String							jarName;

	JarResourceLoader				jarResourceLoader	= null;

	public ModulePluginDriver getDriver() {
		return driver;
	}

	public Class<ModulePluginDriver> getDriverClass() {
		return driverClass;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public boolean isInJar() {
		return inJar;
	}

	public Object getSerializableDriverInfo() {
		return new SerializableDriverInfo(driverClass.getName(), driverClassName, inJar, lastModifyTime, jarName);
	}

	public String toString() {
		return ("driver[className=" + driverClassName + "," + driver + "]");
	}
}