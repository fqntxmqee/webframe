
package org.webframe.core.exception.entity;

import org.webframe.core.exception.ServiceException;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-27
 *          下午01:37:10
 */
public class EntityException extends ServiceException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7855305520085268404L;

	public EntityException(String msg) {
		super(msg);
	}

	public EntityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
