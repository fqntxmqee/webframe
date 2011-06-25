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
package org.webframe.web.page.adapter.hibernate.util.setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.3 $ $Date: 2005/12/19 12:22:26 $
 */
public class TimestampSetter extends AbstractSetter
{
   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(TimestampSetter.class);

   public static final String DEFAULT_FORMAT = "MM/dd/yyyy HH:mm:ss";

   private SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);

   /**
    * @see org.webframe.web.page.adapter.hibernate3.util.Setter#set(Query, String, Object)
    */
   public void set(Query query, String key, Object value) throws HibernateException, ParseException
   {
      Date date = null;
      if (value instanceof String)
      {
         if (LOGGER.isInfoEnabled())
         {
            LOGGER.info("The key='" + key + "'s value is instance of a String, now is parsing to date.");
         }
         date = formatter.parse((String) value);
      }
      else if (value instanceof Date)
      {
         date = (Date) value;
      }
      else if (value == null)
      {
         if (LOGGER.isInfoEnabled())
         {
            LOGGER.info("The key='" + key + "'s value is null.");
         }
      }
      else
      {
         if (LOGGER.isWarnEnabled())
         {
            LOGGER.warn("The key's='" + key + "' value='" + value + "' was expected as Date or String parseable to Date.");
         }
         throw new IllegalArgumentException("Cannot convert value of class " + value.getClass().getName() + " to timestamp (key=" + key
               + ")");
      }

      if (LOGGER.isInfoEnabled())
      {
         LOGGER.info("The key='" + key + "' was set to the query as Timestamp with the value date='" + date + "'.");
      }

      query.setTimestamp(key, date);
   }

   /**
    * @param format The format to set.
    */
   public void setFormat(String format)
   {
      formatter = new SimpleDateFormat(format);
   }
}