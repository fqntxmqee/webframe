
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-25 下午01:58:31
 */
public class DefaultSetter extends AbstractSetter {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER	= LogFactory.getLog(DefaultSetter.class);

	public void set(Query query, String key, Object value) throws HibernateException, ParseException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("The key='" + key + "' was set to the query as the parameter='" + value + "'.");
		}
		query.setParameter(key, value);
	}
}