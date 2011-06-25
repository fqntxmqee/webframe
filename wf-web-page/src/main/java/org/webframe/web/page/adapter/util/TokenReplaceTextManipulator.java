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

import java.util.Collections;
import java.util.Map;

/**
 * Replaces "This is a [test]" with "This is a <%=map.get("test")%>"
 * 
 * @author mwilson
 */
public class TokenReplaceTextManipulator implements TextManipulator {

	private String	startToken	= "[";

	private String	endToken		= "]";

	private String	nullValue	= "";

	/**
	 * @see org.webframe.web.page.adapter.util.TextManipulator#manipulate(java.lang.String,
	 *      java.lang.Object)
	 */
	public StringBuffer manipulate(StringBuffer text, Map<String, Object> model) {
		if (model == null) {
			model = Collections.emptyMap();
		}
		// Replace any [key] with the value in the whereClause Map.
		for (int i = 0, end = 0, start; ((start = text.toString().indexOf(startToken, end)) >= 0); i++) {
			end = text.toString().indexOf(endToken, start);
			String key = text.substring(start + 1, end);
			Object modelValue = model.get(key);
			text.replace(start, end + 1, (modelValue == null) ? nullValue : modelValue.toString());
			end -= (key.length() + 2);
		}
		return text;
	}

	/**
	 * @param endToken The endToken to set.
	 */
	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}

	/**
	 * @param startToken The startToken to set.
	 */
	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}

	/**
	 * @param nullValue The nullValue to set.
	 */
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}
}