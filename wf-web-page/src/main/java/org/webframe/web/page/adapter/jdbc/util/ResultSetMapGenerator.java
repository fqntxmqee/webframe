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

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.3 $ $Date: 2005/08/19 16:06:29 $
 */
public class ResultSetMapGenerator {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER	= LogFactory.getLog(ResultSetMapGenerator.class);

	private ResultSet				result;

	private String[]				names;

	public ResultSetMapGenerator(ResultSet result, boolean useName, boolean lowerCase) throws SQLException {
		this.result = result;
		ResultSetMetaData metadata = result.getMetaData();
		int columnCount = metadata.getColumnCount();
		names = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			names[i] = (useName) ? metadata.getColumnName(i + 1) : metadata.getColumnLabel(i + 1);
			if (names[i] == null || names[i].length() == 0) {
				names[i] = (useName) ? metadata.getColumnLabel(i + 1) : metadata.getColumnName(i + 1);
			}
			if (lowerCase) {
				names[i] = names[i].toLowerCase();
			}
		}
		LOGGER.debug(names);
	}

	/**
	 * @see net.sf.cglib.beans.BeanGenerator#create()
	 */
	public Map<String, Object> generateMap() throws SQLException, NoSuchMethodException, InvocationTargetException,
				IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < names.length; i++) {
			Object value = result.getObject(i + 1);
			map.put(names[i], value);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(names[i] + " - " + value);
			}
		}
		return map;
	}

	/**
	 * @return Returns the result.
	 */
	public ResultSet getResultSet() {
		return result;
	}

	/**
	 * <p>Loads and returns the <code>Class</code> of the given name. By default, a load from the
	 * thread context class loader is attempted. If there is no such class loader, the class loader
	 * used to load this class will be utilized.</p>
	 * 
	 * @exception SQLException if an exception was thrown trying to load the specified class
	 */
	protected Class<?> loadClass(String className) throws SQLException {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl == null) {
				cl = this.getClass().getClassLoader();
			}
			return (cl.loadClass(className));
		} catch (Exception e) {
			throw new SQLException("Cannot load column class '" + className + "': " + e);
		}
	}
}