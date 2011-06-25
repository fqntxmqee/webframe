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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Sets a <code>java.lang.?????</code> on a <code>PreparedStatement</code>.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2004/08/10 20:41:32 $
 */
public interface Setter
{
   /**
   * Set the object on the given query.
   * 
   * @param query The PreparedStatement.
   * @param index The name of the argument
   * @param value The value to be set
   * 
   * @throws SQLException If an error occurs.
   * @throws ParseException If an error occurs.
   */
   int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException;
   
   String getReplacementString(Object value);
}