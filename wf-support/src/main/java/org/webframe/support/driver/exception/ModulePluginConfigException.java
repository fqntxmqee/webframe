
package org.webframe.support.driver.exception;

/**
 * 模块插件驱动详细信息配置异常
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-30 下午01:35:21
 */
public class ModulePluginConfigException extends ModulePluginException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7741164884191044032L;

	public ModulePluginConfigException(String msg) {
		super(msg);
	}

	public ModulePluginConfigException(String msg, Throwable e) {
		super(msg, e);
	}
}
