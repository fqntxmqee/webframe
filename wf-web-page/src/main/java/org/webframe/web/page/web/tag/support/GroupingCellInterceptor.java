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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

/**
 * @author mwilson Groups cell by using lastRow.property.equsls(currentRow.property)
 */
public class GroupingCellInterceptor implements CellInterceptor {

	/**
	 * @see org.webframe.web.page.web.tag.support.CellInterceptor#isHidden(javax.servlet.jsp.PageContext,
	 *      java.lang.String, java.lang.String, java.lang.Object)
	 */
	public boolean isHidden(PageContext pageContext, String key, String name, Object value) {
		if (key == null) {
			return false;
		}
		GroupingInfo info = (GroupingInfo) pageContext.getAttribute(key + "GroupingInfo");
		if (info == null) {
			pageContext.setAttribute(key + "GroupingInfo", info = new GroupingInfo());
		}
		return info.sameAsLastValue(name, value);
	}

	private static class GroupingInfo {

		private List<String>				names			= new ArrayList<String>();

		private Map<String, Object>	lastValues	= new HashMap<String, Object>();

		public boolean sameAsLastValue(String name, Object value) {
			if (!names.contains(name)) {
				names.add(name);
				lastValues.put(name, value);
				return false;
			}
			Object lastValue = lastValues.get(name);
			lastValues.put(name, value);
			if (lastValue == null) {
				return false;
			} else {
				if (lastValue.equals(value)) {
					return true;
				} else {
					boolean nullOut = false;
					for (Iterator<String> iter = names.iterator(); iter.hasNext();) {
						String currentName = iter.next();
						if (nullOut) {
							lastValues.remove(currentName);
						}
						if (currentName.equals(name)) {
							nullOut = true;
						}
					}
					return false;
				}
			}
		}
	}
}