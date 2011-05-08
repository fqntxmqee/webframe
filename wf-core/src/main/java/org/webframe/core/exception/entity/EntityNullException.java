
package org.webframe.core.exception.entity;

import org.webframe.core.model.BaseEntity;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-27
 *          下午01:37:10
 */
public class EntityNullException extends EntityException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7855305520085268404L;

	public EntityNullException(Class<? extends BaseEntity> entityClass) {
		super(entityClass + "类型对象不能为null！");
	}

	public EntityNullException() {
		super("持久化对象不能为null！");
	}
}
