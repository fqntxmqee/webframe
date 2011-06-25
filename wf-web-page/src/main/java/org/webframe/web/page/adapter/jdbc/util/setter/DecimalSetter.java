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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;

/** Sets a <code>java.sql.Types.DECIMAL</code> on a query using <code>PreparedStatement.setBigDecimal()</code>.
 * Conversion is provided from <code>java.math.BigDecimal</code>, <code>java.math.BigInteger</code>,
 * <code>java.lang.Double</code>, <code> java.lang.Long</code>. Conversion from a string is provided
 * using <code>Long.parseLong()</code> method.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2005/12/19 10:58:01 $
 */
public class DecimalSetter extends AbstractSetter
{
   /**
    * @see org.webframe.web.page.adapter.jdbc.util.Setter#set(java.sql.PreparedStatement, int, java.lang.Object)
    */
   public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException
   {
      if (value instanceof BigDecimal)
      {
         query.setBigDecimal(index++, (BigDecimal) value);
      }
      else if (value instanceof BigInteger)
      {
         BigDecimal decimal = new BigDecimal((BigInteger) value);
         query.setBigDecimal(index++, decimal);
      }
      else if (value instanceof Long)
      {
         BigDecimal decimal = BigDecimal.valueOf(((Long) value).longValue());
         query.setBigDecimal(index++, decimal);
      }
      else if (value instanceof Double)
      {
         BigDecimal decimal = new BigDecimal(((Double) value).doubleValue());
         query.setBigDecimal(index++, decimal);
      }
      else if (value instanceof String)
      {
         BigDecimal decimal = new BigDecimal((String) value);
         query.setBigDecimal(index++, decimal);
      }
      else if (value == null)
      {
         query.setNull(index++, Types.DECIMAL);
      }
      else
      {
         throw new IllegalArgumentException("Cannot convert value of class " + value.getClass().getName() + " to decimal at position "
               + index);
      }
      return index;
   }
}