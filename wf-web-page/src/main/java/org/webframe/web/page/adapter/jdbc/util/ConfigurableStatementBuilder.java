/**
 * Copyright (c) 2003 held jointly by the individual authors.            
 *                                                                          
 * This library is free software; you can redistribute it and/or modify it    
 * under the terms of the GNU Lesser General Public License as published      
 * by the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.                                            
 *                                                                            
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; with out even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.                                                  
 *                                                                           
 * You should have received a copy of the GNU Lesser General Public License   
 * along with this library;  if not, write to the Free Software Foundation,   
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.              
 *                                                                            
 * > http://www.gnu.org/copyleft/lesser.html                                  
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.adapter.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.adapter.jdbc.util.setter.StringSetter;
import org.webframe.web.page.adapter.util.FilterTextManipulator;
import org.webframe.web.page.adapter.util.TextManipulator;
import org.webframe.web.page.adapter.util.TokenReplaceTextManipulator;

/**
 * Copyright (c) 2003 held jointly by the individual authors. This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. >
 * http://www.gnu.org/copyleft/lesser.html > http://www.opensource.org/licenses/lgpl-license.php
 */
public class ConfigurableStatementBuilder implements StatementBuilder {

	/** Commons logger. */
	private static final Log		LOGGER			= LogFactory.getLog(ConfigurableStatementBuilder.class);

	private Map<String, Setter>	setters;

	private Setter						defaultSetter	= new StringSetter();

	private List<TextManipulator>	textManipulators;

	private boolean					init				= false;

	/**
	 * Initialize this bean with default values.
	 */
	public void init() {
		if (textManipulators == null) {
			textManipulators = new LinkedList<TextManipulator>();
			textManipulators.add(new FilterTextManipulator());
			textManipulators.add(new TokenReplaceTextManipulator());
		}
		init = true;
	}

	/**
	 * @see org.webframe.web.page.adapter.jdbc.util.StatementBuilder#generate(java.sql.Connection,
	 *      java.lang.StringBuffer, java.util.Map, boolean)
	 */
	public PreparedStatement generate(Connection conn, StringBuffer query, Map<String, Object> whereClause, boolean scrollable)
				throws SQLException, ParseException {
		if (!init) {
			init();
		}
		if (whereClause == null) {
			whereClause = Collections.emptyMap();
		}
		for (Iterator<TextManipulator> iter = textManipulators.iterator(); iter.hasNext();) {
			TextManipulator manipulator = (TextManipulator) iter.next();
			manipulator.manipulate(query, whereClause);
		}
		LinkedList<NamedPair> arguments = new LinkedList<NamedPair>();
		// Replace any "{key}" with the value in the whereClause Map,
		// then placing the value in the partameters list.
		for (int i = 0, end = 0, start; ((start = query.toString().indexOf('{', end)) >= 0); i++) {
			end = query.toString().indexOf('}', start);
			String key = query.substring(start + 1, end);
			Object value = whereClause.get(key);
			if (value == null) {
				throw new NullPointerException("Property '" + key + "' was not provided.");
			}
			arguments.add(new NamedPair(key, value));
			Setter setter = getSetter(key);
			query.replace(start, end + 1, setter.getReplacementString(value));
			end -= (key.length() + 2);
		}
		PreparedStatement statement = null;
		if (scrollable) {
			statement = conn.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		} else {
			statement = conn.prepareStatement(query.toString());
		}
		int index = 1;
		// Now set all the patameters on the statement.
		for (Iterator<NamedPair> iter = arguments.iterator(); iter.hasNext();) {
			NamedPair namedPair = iter.next();
			Setter setter = getSetter(namedPair.getName());
			try {
				index = setter.set(statement, index, namedPair.getValue());
			} catch (RuntimeException e) {
				String message = "Cannot set value of " + namedPair.getName() + " (setter = " + setter + ")";
				LOGGER.error(message, e);
				throw new RuntimeException(message, e);
			}
		}
		return statement;
	}

	/**
	 * Gets a setter for the given property.
	 * 
	 * @param name The name of the property. Also the key in the Map.
	 * @return The setter for the given property.
	 */
	public Setter getSetter(String name) {
		Setter setter = ((setters == null) ? defaultSetter : (Setter) setters.get(name));
		return (setter == null) ? defaultSetter : setter;
	}

	/**
	 * @param setters The setters to set.
	 */
	public void setSetters(Map<String, Setter> setters) {
		this.setters = setters;
	}

	/**
	 * @param defaultSetter The defaultSetter to set.
	 */
	public void setDefaultSetter(Setter defaultSetter) {
		this.defaultSetter = defaultSetter;
	}

	/**
	 * @param textManipulatorList The textManipulatorList to set.
	 */
	public void setTextManipulators(List<TextManipulator> textManipulators) {
		this.textManipulators = textManipulators;
	}
}