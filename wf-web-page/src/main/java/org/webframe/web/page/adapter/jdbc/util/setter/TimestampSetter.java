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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Sets a <code>java.sql.Types.TIMESTAMP</code> on a query using <code>PreparedStatement.setTimestamp()</code>. 
 * Conversion is provided for types <code>java.sql.Timestamp</code>, <code>java.util.Date</code>, <code>java.util.Calendar</code>.
 * Conversion from a string is provided using a formater - the default format is "MM/dd/yyyy".
 * 
 * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.6 $ $Date: 2005/12/19 10:56:41 $
 */
public class TimestampSetter extends AbstractSetter
{
   public static final String DEFAULT_FORMAT = "MM/dd/yyyy";

   protected SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);

   /**
    * @see org.webframe.web.page.adapter.jdbc.util.Setter#set(java.sql.PreparedStatement, int, java.lang.Object)
    */
   public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException
   {

      if (value == null || value instanceof Timestamp)
      {
         Timestamp timestamp = (Timestamp) value;
         query.setTimestamp(index++, timestamp);
      }
      else if (value instanceof Date)
      {
         Date date = (Date) value;
         Timestamp timestamp = new Timestamp(date.getTime());
         query.setTimestamp(index++, timestamp);
      }
      else if (value instanceof Calendar)
      {
         Calendar calendar = (Calendar) value;
         Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
         query.setTimestamp(index++, timestamp);
      }
      else if (value instanceof String)
      {
         Date date = formatter.parse((String) value);
         Timestamp timestamp = new Timestamp(date.getTime());
         query.setTimestamp(index++, timestamp);
      }
      else
      {
         throw new IllegalArgumentException("Cannot convert object of type " + value.getClass().getName() + " to timestamp at position "
               + index);
      }

      return index;
   }

   /**
    * @param format The format to set.
    */
   public void setFormat(String format)
   {
      formatter = new SimpleDateFormat(format);
   }
}