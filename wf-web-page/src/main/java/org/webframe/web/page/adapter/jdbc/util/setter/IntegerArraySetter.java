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

/** Consumes a String[] and sets multiple Integer(s) on the Statement. 
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.6 $ $Date: 2005/12/19 10:58:01 $
 */
public class IntegerArraySetter extends AbstractArraySetter
{
	/**
	 * @see org.webframe.web.page.adapter.jdbc.util.Setter#set(java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException
	{
		if (isUseBindVarables())
		{
			String[] values = (value instanceof String[]) ? (String[]) value : new String[] { (String) value
			};
			for (int i = 0, length = values.length; i < length; i++)
			{
				
				query.setInt(index++, Integer.parseInt(values[i]));
			}
		}
		
		return index;
	}
}