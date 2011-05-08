
package org.webframe.core.hibernate;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.HSQLDialect;

/**
 * 解决通过 Hibernate createSQLQuery() 方法进行查询，对应表中的列有 BOOLEAN类型的，HSQL方言导致的。
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-22 下午04:49:55
 */
public class HSQLDialectExtend extends HSQLDialect {

	public HSQLDialectExtend() {
		super();
		registerHibernateType(Types.BOOLEAN, Hibernate.BOOLEAN.getName());
	}
}
