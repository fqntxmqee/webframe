/**
 * Copyright (c) 2003 held jointly by the individual authors.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. >
 * http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */
package org.webframe.web.page.adapter.jdbc.util.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sets a <code>Calendar</code> using a <code>java.sql.Timestamp</code> on a
 * <code>Query</code>. This could be usefull when you are using hibernate
 * mapping and accesing directly via jdbc.
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.3 $ $Date: 2005/12/19 10:56:41 $
 */
public class Hibernate20CalendarSetter extends AbstractSetter
{
   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(Hibernate20CalendarSetter.class);

   /**
    * <ol>
    * <li>If filter value is instance of the Calendar, it will set it directly
    * to <code>PreparedStatement</code> query. </li>
    * <li>Otherwise it will set new Timestamp(with actual time) to query for
    * key.</li>
    * </ol>
    * 
    * @see net.mlw.util.sql.StatementBuilder.Setter#set(java.sql.PreparedStatement,
    *      int, java.lang.Object)
    */
   public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException
   {
      long timeInMillis = 0;
      if (value instanceof Calendar)
      {
         timeInMillis = ((Calendar) value).getTimeInMillis();
      }
      else
      {
         if (LOGGER.isWarnEnabled())
         {
            LOGGER.warn("The key's index's='" + index + "' value='" + value + "' was expected as Calendar. Using actual time.");
         }
         timeInMillis = Calendar.getInstance().getTimeInMillis();
      }

      query.setTimestamp(index++, new Timestamp(timeInMillis));
      return index;
   }
}