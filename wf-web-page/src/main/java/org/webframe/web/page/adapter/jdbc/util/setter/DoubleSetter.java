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
import java.sql.Types;
import java.text.ParseException;

/** Sets a <code>Double</code> on a query. Conversion from a string is provided using <code>Double.parseDouble()</code>.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.4 $ $Date: 2005/12/19 10:58:01 $
 */
public class DoubleSetter extends AbstractSetter
{
   /**
    * @see org.webframe.web.page.adapter.jdbc.util.Setter#set(java.sql.PreparedStatement, int, java.lang.Object)
    */
   public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException
   {
      if (value instanceof Double)
      {
         query.setDouble(index++, ((Double) value).doubleValue());
      }
      else if (value instanceof String)
      {
         double doubleValue = Double.parseDouble((String) value);
         query.setDouble(index++, doubleValue);
      }
      else if (value == null)
      {
         query.setNull(index++, Types.DOUBLE);
      }
      else
      {
         throw new IllegalArgumentException("Cannot convert value of class " + value.getClass().getName() + " to double at position "
               + index);
      }
      return index;
   }
}