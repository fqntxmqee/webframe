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
import java.text.SimpleDateFormat;
import java.util.Date;

/** This setter is a ahck to fix problems with the SAS JDBC driver...
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2005/12/19 10:56:41 $
 */
public class SasDateSetter extends TimestampSetter
{
   protected SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);

   public static final String DEFAULT_SAS_FORMAT = "ddMMMyy";

   protected SimpleDateFormat sasFormatter = new SimpleDateFormat(DEFAULT_SAS_FORMAT);

   /**
    * @see net.mlw.util.sql.StatementBuilder.Setter#set(java.sql.PreparedStatement, int, java.lang.Object)
    */
   public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException
   {
      //Parse the date into a normal java.util.Date
      Date date = formatter.parse((String) value);

      //Convert the date into a string and set it.
      query.setString(index++, sasFormatter.format(date));
      return index;
   }

   /**
    * @param format The format to set.
    */
   public void setSasDateFormat(String format)
   {
      sasFormatter = new SimpleDateFormat(format);
   }
}