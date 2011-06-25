
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * Sets a <code>Integer</code> on a <code>Query</code>. Conversion from a string is provided using
 * <code>Integer.parseInt()</code>.
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午01:59:03
 */
public class IntegerSetter extends AbstractSetter {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER	= LogFactory.getLog(IntegerSetter.class);

	/**
	 * @see org.webframe.web.page.adapter.hibernate3.util.Setter#set(Query, String, Object)
	 */
	public void set(Query query, String key, Object value) throws HibernateException, ParseException {
		if (value instanceof Integer) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is instance of a Integer, now is parsing to int.");
			}
			int intValue = ((Integer) value).intValue();
			query.setInteger(key, intValue);
		} else if (value instanceof String) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is instance of a String, now is parsing to int.");
			}
			int intValue = Integer.parseInt((String) value);
			query.setInteger(key, intValue);
		} else if (value == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is null.");
			}
			query.setParameter(key, null);
		} else {
			throw new IllegalArgumentException("Cannot convert value of class "
						+ value.getClass().getName()
						+ " to int (key="
						+ key
						+ ")");
		}
	}
}