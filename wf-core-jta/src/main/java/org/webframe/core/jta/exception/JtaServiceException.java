/*
 * wf-core-jta
 * Created on 2011-6-29-下午09:29:52
 */

package org.webframe.core.jta.exception;

import org.webframe.core.exception.ServiceException;

/**
 * JTA业务异常类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午09:29:52
 */
public class JtaServiceException extends ServiceException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 85081022021406843L;

	public JtaServiceException(String msg, Throwable cause) {
		super("JTA: " + msg, cause);
	}

	public JtaServiceException(String msg) {
		super("JTA: " + msg);
	}
}
