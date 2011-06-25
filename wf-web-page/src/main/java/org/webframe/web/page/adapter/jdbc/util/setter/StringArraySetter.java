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

package org.webframe.web.page.adapter.jdbc.util.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Consumes a String[] and sets multiple String(s) on the Statement.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.6 $ $Date: 2005/12/19 10:58:01 $
 */
public class StringArraySetter extends AbstractArraySetter {

	private String	token;

	/**
	 * @see org.webframe.web.page.adapter.jdbc.util.Setter#getReplacementString(java.lang.Object)
	 */
	public String getReplacementString(Object value) {
		if (token != null && (value instanceof String)) {
			List<String> tokens = new ArrayList<String>();
			for (StringTokenizer st = new StringTokenizer((String) value, token); st.hasMoreTokens();) {
				tokens.add(st.nextToken());
			}
			value = (String[]) tokens.toArray(new String[]{});
		}
		return super.getReplacementString(value);
	}

	/**
	 * @see org.webframe.web.page.adapter.jdbc.util.Setter#set(java.sql.PreparedStatement, int,
	 *      java.lang.Object)
	 */
	public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException {
		String[] values = (value instanceof String[]) ? (String[]) value : new String[]{
			(String) value};
		if (token != null && (value instanceof String)) {
			List<String> tokens = new ArrayList<String>();
			for (StringTokenizer st = new StringTokenizer((String) value, token); st.hasMoreTokens();) {
				String token = st.nextToken();
				tokens.add(token);
			}
			values = (String[]) tokens.toArray(new String[]{});
		}
		for (int i = 0, length = values.length; i < length; i++) {
			query.setString(index++, values[i]);
		}
		return index;
	}

	/**
	 * @param token The token to set.
	 */
	public void setToken(String token) {
		this.token = token;
	}
}