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

package org.webframe.web.page.adapter.util;

import java.util.Map;

/**
 * This interface consumes a StringBuffer and a Map and manipulates the StringBuffer.
 * org.webframe.web.page.adapter.util.StringDotReplaceAllTextManipulator
 * 
 * @author mwilson
 */
public class StringDotReplaceAllTextManipulator implements TextManipulator {

	private String	regex;

	private String	replacement;

	/**
	 * @param regex The regex to set.
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @param replacement The replacement to set.
	 */
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	/**
	 * @see org.webframe.web.page.adapter.util.TextManipulator#manipulate(java.lang.StringBuffer,
	 *      java.util.Map)
	 */
	public StringBuffer manipulate(StringBuffer value, Map<String, Object> model) {
		String text = value.toString();
		value.replace(0, text.length(), "");
		value.append(text.replaceAll(regex, replacement));
		return value;
	}
}