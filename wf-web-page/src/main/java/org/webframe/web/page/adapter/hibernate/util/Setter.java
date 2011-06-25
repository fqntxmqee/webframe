
package org.webframe.web.page.adapter.hibernate.util;

import java.text.ParseException;

import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * Sets a <code>java.lang.</code> on a <code>PreparedStatement</code>.
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午02:02:26
 */
public interface Setter {

	/**
	 * Set the object on the given query.
	 * 
	 * @param query The Hiberante Query.
	 * @param key The name of the argument
	 * @param value The value to be set
	 * @throws ParseException If an error occurs.
	 */
	void set(Query query, String key, Object value) throws HibernateException, ParseException;
}