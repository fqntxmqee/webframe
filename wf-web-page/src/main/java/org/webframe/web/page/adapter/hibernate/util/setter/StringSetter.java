
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * Sets a <code>java.lang.String</code> on a <code>Query</code>.
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午02:00:00
 */
public class StringSetter extends AbstractSetter {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER	= LogFactory.getLog(StringSetter.class);

	/**
	 * @see org.webframe.web.page.adapter.hibernate3.util.Setter#set(Query, String, Object)
	 */
	public void set(Query query, String key, Object value) throws HibernateException, ParseException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("The key='" + key + "' was set to the query as the String='" + value + "'.");
		}
		try {
			query.setString(key, (String) value);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Failed setting a bind variable to a statement because "
						+ "it's of another class than the setter is made for - the variable is "
						+ value.getClass()
						+ " while the setter is "
						+ getClass().getName()
						+ ". Set the correct setter in value list config file - see for example "
						+ "org.webframe.web.page.adapter.jdbc.util.setter.AbstractSetter.");
		}
	}
}