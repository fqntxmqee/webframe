
package org.webframe.support.driver.exception;

/**
 * 模块插件驱动不存在异常
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:30:49
 */
public class DriverNotExistException extends ModulePluginException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1354806416368944079L;

	public DriverNotExistException() {
		super("模块插件驱动类全路径不能为null！");
	}

	public DriverNotExistException(String driverPath) {
		super("模块插件驱动(" + driverPath + ")不存在！");
	}
}
