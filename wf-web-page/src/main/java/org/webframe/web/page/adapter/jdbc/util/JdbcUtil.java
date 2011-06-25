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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mwilson
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class JdbcUtil
{
   /** Commons Logger */
   private static final Log LOGGER = LogFactory.getFactory().getInstance(JdbcUtil.class);

   /** Protect singleton */
   private JdbcUtil()
   {

   }

   /** Cleans up resources.
    * 
    * @param statement Statement to close.
    * @param connection Connection to close.
    */
   public static void close(Statement statement, Connection connection)
   {
      try
      {
         statement.close();
      }
      catch (Exception ignore)
      {
         LOGGER.info(ignore);
      }
      try
      {
         connection.close();
      }
      catch (Exception ignore)
      {
         LOGGER.info(ignore);
      }
   }

   /** Cleans up resources.
    * 
    * @param result ResultSet to close.
    * @param statement Statement to close.
    * @param connection Connection to close.
    */
   public static void close(ResultSet result, Statement statement, Connection connection)
   {
      if (result != null)
         try
         {
            result.close();
         }
         catch (Exception ignore)
         {
            LOGGER.info(ignore);
         }
      if (statement != null)
         try
         {
            statement.close();
         }
         catch (Exception ignore)
         {
            LOGGER.info(ignore);
         }
      if (connection != null)
         try
         {
            connection.close();
         }
         catch (Exception ignore)
         {
            LOGGER.info(ignore);
         }
   }
}