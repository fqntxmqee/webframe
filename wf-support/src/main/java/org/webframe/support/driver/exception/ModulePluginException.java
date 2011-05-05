/*
 * wf-support
 * Created on 2011-5-5-下午04:23:56
 */

package org.webframe.support.driver.exception;

/**
 * 模块插件驱动异常
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午04:23:56
 */
public class ModulePluginException extends RuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3486271067642418684L;

	public ModulePluginException(String msg) {
		super(msg);
	}

	public ModulePluginException(String msg, Throwable e) {
		super(msg, e);
	}
}
