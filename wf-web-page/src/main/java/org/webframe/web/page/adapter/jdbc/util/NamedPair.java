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

/** A Simple class that contains a key and a value.
 * @todo: There has to be in the standard
 *  library a class that can do this.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2004/08/10 20:41:32 $
 */
public class NamedPair
{
	/** The name. */
   private String name;
   /** The value. */
   private Object value;

   /** Default constructor.
    * @param name The name of the property.
    * @param value The value of the property.
    */
   public NamedPair(String name, Object value)
   {
      this.name = name;
      this.value = value;
   }

   /** Gets the name of the property.
    * @return Returns the name.
    */
   public String getName()
   {
      return name;
   }

   /** Gets the value of the property.
    * @return Returns the value.
    */
   public Object getValue()
   {
      return value;
   }
}