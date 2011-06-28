
package org.webframe.web.exception;

/**
 * Web框架顶级异常类，框架所以自定义异常需集成该类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:20:44
 */
public class WebFrameException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2536571070207241036L;

	public WebFrameException(String msg) {
		super("========>>> " + msg);
	}

	public WebFrameException(String msg, Throwable cause) {
		super("========>>> " + msg, cause);
	}
}
