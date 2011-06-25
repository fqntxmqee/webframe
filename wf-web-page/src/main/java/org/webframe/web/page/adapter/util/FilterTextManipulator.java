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
import java.util.StringTokenizer;

/**
 * Includes "filters" if the key is present in the filter tag. For example: <p>
 * "I am a /~size:FAT~/ man." <ul> <li>Would produce "I am a FAT man." if size was a key in the
 * map.</li> <li>Would produce "I am a  man." if size was <b>not</b> a key in the map.</li> </ul>
 * </p>
 * 
 * @author mwilson
 */
public class FilterTextManipulator implements TextManipulator {

	private String	startToken	= "/~";

	private String	endToken		= "~/";

	/**
	 * @see org.webframe.web.page.adapter.util.TextManipulator#manipulate(java.lang.String,
	 *      java.lang.Object)
	 */
	public StringBuffer manipulate(StringBuffer text, Map<String, Object> model) {
		if (model == null) {
			model = Collections.emptyMap();
		}
		boolean inverse = false;
		for (int start = 0, end = text.indexOf(endToken); ((end = text.indexOf(endToken)) >= 0);) {
			start = text.lastIndexOf(startToken, end);
			String key = text.substring(start + 2, text.indexOf(":", start));
			// If this is an or statement
			if (key.indexOf(',') != -1) {
				for (StringTokenizer st = new StringTokenizer(key, ","); st.hasMoreElements();) {
					Object modelValue = model.get((key = st.nextToken()));
					if (modelValue instanceof String && (((String) modelValue).length() == 0)) {
						continue;
					} else if (modelValue != null) {
						break;
					}
				}
			} else if (key.indexOf('|') != -1) {
				for (StringTokenizer st = new StringTokenizer(key, "|"); st.hasMoreElements();) {
					Object modelValue = model.get((key = st.nextToken()));
					if (modelValue instanceof String && (((String) modelValue).length() == 0)) {
						continue;
					} else if (modelValue != null) {
						break;
					}
				}
			} else if (key.indexOf('&') != -1) {
				for (StringTokenizer st = new StringTokenizer(key, "&"); st.hasMoreElements();) {
					Object modelValue = model.get((key = st.nextToken()));
					if (modelValue == null || modelValue instanceof String && (((String) modelValue).length() == 0)) {
						break;
					}
				}
			}
			if ((inverse = key.startsWith("!"))) {
				key = key.substring(1, key.length());
			}
			Object modelValue = model.get(key);
			// If its an empty string replace it with a null.
			if (modelValue instanceof String && (((String) modelValue).length() == 0)) {
				modelValue = null;
			}
			if (inverse) {
				if ((modelValue == null)) {
					text.replace(start, end + 2, text.substring(text.indexOf(":", start) + 1, end));
				} else {
					text.replace(start, end + 2, "");
				}
			} else {
				if ((modelValue != null)) {
					text.replace(start, end + 2, text.substring(text.indexOf(":", start) + 1, end));
				} else {
					text.replace(start, end + 2, "");
				}
			}
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
}