
package org.webframe.core.hibernate;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: MySQL5DialectExtend.java,v 1.1.2.1 2010/04/23 02:49:37 huangguoqing Exp $ Create:
 *          2010-4-23 上午10:44:09
 */
public class MySQL5DialectExtend extends MySQL5Dialect {

	public MySQL5DialectExtend() {
		super();
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
	}
}
