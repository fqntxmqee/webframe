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

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2006/04/18 17:14:19 $
 */
public class SqlPagingSupport
{
   public static final String ORACLE = "oracle";
   
   private String pagedQueryPreSql;
   private String pagedQueryPostSql;
   
   public StringBuffer getPagedQuery(String sql)
   {
      StringBuffer buffer = new StringBuffer(500);
      buffer.append(pagedQueryPreSql);
      buffer.append(sql);
      buffer.append(pagedQueryPostSql);
      
      return buffer;
   }
   
   public StringBuffer getCountQuery(String sql)
   {
      StringBuffer buffer = new StringBuffer(sql.length() + 100);
      return buffer.append("SELECT count(*) FROM (").append(sql).append(")");
   }
   
   public void setDatabase(String database)
   {
      if(ORACLE.equalsIgnoreCase(database))
      {
         pagedQueryPreSql = "SELECT * FROM (SELECT INNER.*, ROWNUM as RECORDNUM FROM (";
         pagedQueryPostSql = ") INNER ) WRAPPED WHERE WRAPPED.RECORDNUM BETWEEN (([pagingPage]-1)*[pagingNumberPer]+1) AND (([pagingPage]-1)*[pagingNumberPer]+[pagingNumberPer])";
      }
      else
      {
         throw new NullPointerException(database + " is not supported ("+ORACLE+").");
      }
   }
   
   public void setPagedQueryPostSql(String pagedQueryPostSql)
   {
      this.pagedQueryPostSql = pagedQueryPostSql;
   }
   public void setPagedQueryPreSql(String pagedQueryPreSql)
   {
      this.pagedQueryPreSql = pagedQueryPreSql;
   }
}
