
package org.webframe.core.exception.entity;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-27
 *          下午01:38:53
 */
public class EntityNotExistException extends EntityException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4642366097588483724L;

	public EntityNotExistException(String msg) {
		super(msg);
	}
}
