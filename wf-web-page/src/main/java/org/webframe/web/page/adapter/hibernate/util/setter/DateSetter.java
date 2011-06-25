
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午01:58:21
 */
public class DateSetter extends AbstractSetter {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER			= LogFactory.getLog(DateSetter.class);

	public static final String	DEFAULT_FORMAT	= "MM/dd/yyyy";

	private SimpleDateFormat	formatter		= new SimpleDateFormat(DEFAULT_FORMAT);

	/**
	 * <ol> <li>If is value instance of the String, it try to parse value using SimpleDateFormat with
	 * specified format.</li> <li>If is value instance of the Date, it will set it directly to query
	 * . </li> <li>Otherwise it will set null to query for key.</li> </ol>
	 * 
	 * @see org.webframe.web.page.adapter.hibernate3.util.Setter#set(org.hibernate.Query, java.lang.String,
	 *      java.lang.Object)
	 * @see #setFormat(String)
	 */
	public void set(Query query, String key, Object value) throws HibernateException, ParseException {
		Date date = null;
		if (value instanceof String) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is instance of a String, now is parsing to date.");
			}
			date = formatter.parse((String) value);
		} else if (value instanceof Date) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "' is instance of a Date.");
			}
			date = (Date) value;
		} else if (value == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is null.");
			}
		} else {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("The key's='"
							+ key
							+ "' value='"
							+ value
							+ "' was expected as Date or String parseable to Date.");
			}
			throw new IllegalArgumentException("Cannot convert value of class "
						+ value.getClass().getName()
						+ " to date (key="
						+ key
						+ ")");
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("The key='" + key + "' was set to the query as Date with the date='" + date + "'.");
		}
		query.setDate(key, date);
	}

	/**
	 * Set the format for parsing <code>Date</code> from string values of keys. Default is
	 * "MM/dd/yyyy"
	 * 
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		formatter = new SimpleDateFormat(format);
	}
}