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

package org.webframe.web.page;

/**
 * @author mwilson, azachar
 */
public interface ValueListHandler {

	/**
	 * The name that should be used to bind this service to a JNDI tree or the like
	 **/
	static String	DEFAULT_SERVICE_NAME	= "valueListHandler";

	/**
	 * Gets a ValueList with the given name.
	 * 
	 * @param name The name of the <CODE>ValueList</CODE>
	 * @param info The <CODE>ValueListInfo</CODE> information.
	 * @see org.webframe.web.page.ValueListHandler#getPostProcessedValueList(ValueList, int)
	 * @return The <CODE>ValueList</CODE>.
	 */
	<X> ValueList<X> getValueList(String name, ValueListInfo info);
}
