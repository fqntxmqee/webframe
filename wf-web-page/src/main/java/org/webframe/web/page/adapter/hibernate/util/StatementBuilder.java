
package org.webframe.web.page.adapter.hibernate.util;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.webframe.web.page.adapter.hibernate.util.setter.DefaultSetter;

/**
 * Utility for working with hibernate.
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.2 $ $Date: 2005/12/16 15:40:09 $
 */
public class StatementBuilder {

	/** Commons Logger */
	private static final Log		LOGGER			= LogFactory.getFactory().getInstance(StatementBuilder.class);

	private Map<String, Setter>	setters;

	private Setter						defaultSetter	= new DefaultSetter();

	/**
	 * Usage of filters: {key} -> :keyName add to query's parameter map keyValue [key] -> keyValue
	 * 
	 * @param hql
	 * @param whereClause
	 * @return Query for ordinary list
	 * @throws HibernateException
	 * @throws ParseException
	 */
	public Query generate(Session session, StringBuffer hql, Map<String, Object> whereClause, boolean isRemoveEmptyStrings)
				throws HibernateException, ParseException {
		if (whereClause == null) {
			whereClause = Collections.emptyMap();
		}
		Map<String, Object> arguments = new HashMap<String, Object>();
		// Include or exclude the filters.
		for (int i = 0, end = 0, start; ((start = hql.toString().indexOf("/~", end)) >= 0); i++) {
			end = hql.toString().indexOf("~/", start);
			String key = hql.substring(start + 2, hql.indexOf(":", start));
			Object value = whereClause.get(key);
			if (isValuePopulated(value, isRemoveEmptyStrings)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("The filter key=["
								+ key
								+ "] with the value=["
								+ value
								+ "] is accepted by the hql preprocesor");
				}
				hql.replace(start, end + 2, hql.substring(hql.indexOf(":", start) + 1, end));
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("The filter key=[" + key + "] is removed from the query by the hql preprocesor.");
				}
				hql.replace(start, end + 2, "");
			}
			end -= start;
		}
		// Replace any [key] with the value in the whereClause Map.
		for (int i = 0, end = 0, start; ((start = hql.toString().indexOf('[', end)) >= 0); i++) {
			end = hql.toString().indexOf(']', start);
			String key = hql.substring(start + 1, end);
			Object value = whereClause.get(key);
			hql.replace(start, end + 1, (value == null) ? "" : value.toString());
			end -= (key.length() + 2);
		}
		// Replace any "{key}" with the value in the whereClause Map,
		// then placing the value in the partameters list.
		for (int i = 0, end = 0, start; ((start = hql.toString().indexOf('{', end)) >= 0); i++) {
			end = hql.toString().indexOf('}', start);
			String key = hql.substring(start + 1, end);
			Object value = whereClause.get(key);
			if (value == null) {
				throw new NullPointerException("Property '" + key + "' was not provided.");
			}
			arguments.put(key, value);
			hql.replace(start, end + 1, ":" + key);
			end -= (key.length() + 2);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("The final query is " + hql);
		}
		Query query = session.createQuery(hql.toString());
		// Now set all the patameters on the statement.
		if (setters == null) {
			for (Iterator<String> keys = arguments.keySet().iterator(); keys.hasNext();) {
				String key = keys.next();
				Object value = arguments.get(key);
				if (value instanceof List) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Setting a paremeterList to the query.");
					}
					query.setParameterList(key, ((List<?>) value).toArray());
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Using the default setter for key=[" + key + "] with the value=[" + value + "]");
					}
					getDefaultSetter().set(query, key, value);
				}
			}
		} else {
			for (Iterator<String> keys = arguments.keySet().iterator(); keys.hasNext();) {
				String key = (String) keys.next();
				Object value = arguments.get(key);
				getSetter(key).set(query, key, value);
			}
		}
		return query;
	}

	/**
	 * @param value
	 * @param isRemoveEmptyStrings Enable/Disable String length checking
	 * @return true - When is it not null and for instances of String is lenght > 0 as well. false -
	 *         When is null, or String is ""
	 */
	private boolean isValuePopulated(Object value, boolean isRemoveEmptyStrings) {
		if (value == null) {
			return false;
		} else {
			if (isRemoveEmptyStrings && value instanceof String) {
				return ((String) value).length() > 0;
			} else {
				return true;
			}
		}
	}

	/**
	 * Generete optimalized query for focusing large amount of data.
	 * 
	 * @param session
	 * @param hql
	 * @param whereClause
	 * @param isRemoveEmptyStrings
	 * @param defaultFocusPropertyObjectAlias
	 * @param focusProperty
	 * @return
	 * @throws HibernateException
	 * @throws ParseException
	 */
	public Query generateForFocus(Session session, StringBuffer hql, Map<String, Object> whereClause, boolean isRemoveEmptyStrings, String defaultFocusPropertyObjectAlias, String focusProperty)
				throws HibernateException, ParseException {
		StringBuffer hsqlFocus = new StringBuffer("SELECT ");
		hsqlFocus.append(defaultFocusPropertyObjectAlias);
		hsqlFocus.append(focusProperty);
		int indexOfTextFrom = hql.toString().toLowerCase().indexOf(" from ");
		if (indexOfTextFrom < 0) {
			indexOfTextFrom = hql.toString().toLowerCase().indexOf("from ");
			hsqlFocus.append(" ");
		}
		if (indexOfTextFrom > -1) {
			hsqlFocus.append(hql.substring(indexOfTextFrom));
			return generate(session, hsqlFocus, whereClause, isRemoveEmptyStrings);
		} else {
			LOGGER.error("HQL hasn't command FROM!!");
			return null;
		}
	}

	/**
	 * @param setters The setters to set.
	 */
	public void setSetters(Map<String, Setter> setters) {
		this.setters = setters;
	}

	public Setter getSetter(String key) {
		Setter setter = null;
		if (setters != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Using custom setters for key=[" + key + "]");
			}
			setter = setters.get(key);
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Using default setter for key=[" + key + "]");
			}
		}
		return (setter == null) ? defaultSetter : setter;
	}

	/**
	 * @return Returns the defaultSetter.
	 */
	public Setter getDefaultSetter() {
		return defaultSetter;
	}

	/**
	 * @param defaultSetter The defaultSetter to set.
	 */
	public void setDefaultSetter(Setter defaultSetter) {
		this.defaultSetter = defaultSetter;
	}
}