
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午01:58:13
 */
public class CalendarSetter extends AbstractSetter {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER	= LogFactory.getLog(CalendarSetter.class);

	/**
	 * <ol> <li>If is filter value instance of the Calendar, it will set it directly to query. </li>
	 * <li>If is filter value instance of String, it try to convert it it to long and set to Calendar
	 * instance</li> <li>If is filter value instance of Long, it try to convert it it to long and set
	 * to Calendar instance</li> <li>Otherwise it will set null to query for key.</li> </ol>
	 * 
	 * @see org.webframe.web.page.adapter.hibernate3.util.Setter#set(org.hibernate.Query, java.lang.String,
	 *      java.lang.Object)
	 */
	public void set(Query query, String key, Object value) throws HibernateException, ParseException {
		Calendar calendar = null;
		if (value instanceof String) {
			calendar = Calendar.getInstance();
			try {
				calendar.setTimeInMillis(Long.valueOf((String) value).longValue());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("The key's='" + key + "' String value='" + value + "' was converted to Calendar.");
				}
			} catch (NumberFormatException e) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("The key's='"
								+ key
								+ "' String value='"
								+ value
								+ "' was not converted to Calendar, error was:"
								+ e.getMessage());
				}
				throw e;
			}
		} else if (value instanceof Long) {
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(((Long) value).longValue());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("The key's='" + key + "' Long value='" + value + "' was converted to Calendar.");
			}
		} else if (value instanceof Calendar) {
			calendar = (Calendar) value;
		} else if (value == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The key='" + key + "'s value is null.");
			}
		} else {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("The key's='" + key + "' value='" + value + "' was expected as Calendar.");
			}
			throw new IllegalArgumentException("Cannot convert value of class "
						+ value.getClass().getName()
						+ " to calendar (key="
						+ key
						+ ")");
		}
		query.setCalendar(key, calendar);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("The key='" + key + "' was set to the query as Calendar='" + calendar + "'.");
		}
	}
}