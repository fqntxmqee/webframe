/**
 * Copyright (c) 2003 held jointly by the individual authors.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. >
 * http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.web.tag.support;

import java.util.HashMap;
import java.util.Map;

import org.webframe.web.page.web.util.JspUtils;

/**
 * Holds a map of attributes.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.4 $ $Date: 2005/08/19 16:06:30 $
 */
public class Attributes implements Attributeable {

	private Map<String, String>	attributes	= new HashMap<String, String>();

	/**
	 * Default constructore.
	 */
	public Attributes() {
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.Attributeable#setAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public void setCellAttribute(String name, String value) {
		attributes.put(name, value);
	}

	/**
	 * returns a String in the form or nameX="valueX".
	 * 
	 * @return A String in the form or nameX="valueX".
	 */
	public String getCellAttributesAsString() {
		return JspUtils.toAttributesString(attributes);
	}

	public Map<String, String> getMap() {
		return attributes;
	}

	/**
	 * reset this bean.
	 */
	public void reset() {
		attributes.clear();
	}

	/**
	 * Append with the space to existing non null or empty key's value. If not exist key, uses
	 * setCellAttribute Ussually used with "class" attributes.
	 * 
	 * @param key
	 * @param style to append
	 * @see #setCellAttribute(String, String)
	 */
	public void appendCellAttribute(String key, String style) {
		if (attributes.containsKey(key)) {
			String current = (String) attributes.get(key);
			if (current != null && current.length() > 0) {
				attributes.put(key, current + " " + style);
			} else {
				setCellAttribute(key, style);
			}
		} else {
			setCellAttribute(key, style);
		}
	}
}