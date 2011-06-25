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
package org.webframe.web.page.adapter.jdbc;


/**
 * This adapter handles the standard functionality of creating a query and
 * execution it...
 * 
 * org.webframe.web.page.adapter.jdbc.AbstractJdbcAdapter
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.2 $ $Date: 2005/08/19 16:06:29 $
 */
public abstract class AbstractDynaJdbcAdapter extends AbstractJdbcAdapter
{
   /** Sets weather the name of the column, or the alias of the column is used. * */
   private boolean useName = false;

   private boolean lowerCase = false;

   public AbstractDynaJdbcAdapter()
   {
   }

   public boolean isUseName()
   {
      return useName;
   }

   /**
    * Sets weather the name of the column, or the alias of the column is used.
    * For example:
    * <p>
    * SELECT X as Y from dual; X = name Y = alias
    * </p>
    * 
    * @param useName
    *            true: use the name of the column false: use the name of the
    *            alias
    */
   public void setUseName(boolean useName)

   {
      this.useName = useName;
   }

   public boolean isLowerCase()
   {
      return lowerCase;
   }

   /**
    * Sets weather the name of the column should be lowecase;
    * 
    * @param lowerCase
    */
   public void setLowerCase(boolean lowerCase)
   {
      this.lowerCase = lowerCase;
   }
}