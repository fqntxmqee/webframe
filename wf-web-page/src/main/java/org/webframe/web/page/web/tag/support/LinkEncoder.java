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

package org.webframe.web.page.web.tag.support;

import java.util.Map;

import javax.servlet.jsp.PageContext;

/**
 * This interface defines a means to encode a url. This will allow for protlets and other unknow
 * enviroments.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.5 $ $Date: 2004/08/17 15:30:50 $
 */
public interface LinkEncoder {

	/**
	 * Returns an encoded String from the given parameters.
	 * 
	 * @param pageContext The PageContext to assist if needed.
	 * @param parameters A Map containing all the parameters to encode.
	 * @param includedKeys The parameters to include. Includes all if null or empty.
	 * @param ignoredKeys The parameters to exclude. Excludes none if null or empty.
	 * @return An encoded String
	 */
	String encode(PageContext pageContext, Map<String, Object> parameters);
}