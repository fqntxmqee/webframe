
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * Sets a <code>Double</code> on a <code>Query</code>. Conversion from a string is provided using
 * <code>Double.parseDouble()</code>.
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午01:58:49
 */
public class DoubleSetter extends AbstractSetter {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER	= LogFactory.getLog(DoubleSetter.class);

	/**
	 * @see org.webframe.web.page.adapter.hibernate3.util.Setter#set(Query, String, Object)
	 */
	public void set(Query query, String key, Object value) throws HibernateException, ParseException {
		if (value instanceof Double) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is instance of a Double, now is converting to double.");
			}
			double doubleValue = ((Double) value).doubleValue();
			query.setDouble(key, doubleValue);
		} else if (value instanceof String) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is instance of a String, now is parsing to double.");
			}
			double doubleValue = Double.parseDouble((String) value);
			query.setDouble(key, doubleValue);
		} else if (value == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is null.");
			}
			query.setParameter(key, null);
		} else {
			throw new IllegalArgumentException("Cannot convert value of class "
						+ value.getClass().getName()
						+ " to double (key="
						+ key
						+ ")");
		}
	}
}