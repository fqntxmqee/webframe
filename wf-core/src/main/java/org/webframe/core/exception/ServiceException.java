
package org.webframe.core.exception;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-25
 *          下午05:56:04
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 630644362320650153L;

	public ServiceException(String msg) {
		super("========>>> " + msg);
	}

	public ServiceException(String msg, Throwable cause) {
		super("========>>> " + msg, cause);
	}
}
