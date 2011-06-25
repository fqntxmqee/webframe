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

package org.webframe.web.page.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.webframe.web.page.web.tag.support.Attributeable;
import org.webframe.web.page.web.tag.support.Attributes;

/**
 * @author mwilson, azachar
 * @todo refactor resetCellAtributes to resetAttributes and include attributesString = null.
 */
public class ConfigurableTag extends BodyTagSupport implements Attributeable {

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 1941985779173022883L;

	private Attributes			cellAttributes					= new Attributes();

	private String					attributesString				= null;

	private String					initialClassCellAttributes	= null;

	/**
	 * It sets initialClassCellAttibutes, if found as name 'class'
	 * 
	 * @see org.webframe.web.page.web.tag.support.Attributeable#setCellAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public void setCellAttribute(String name, String value) {
		cellAttributes.setCellAttribute(name, value);
		if (name.equalsIgnoreCase("class")) {
			initialClassCellAttributes = value;
		}
	}

	/**
	 * Append to initialClassCellAttibutes this class attribute value
	 * 
	 * @param value The class attribute value
	 */
	public void appendClassCellAttribute(String value) {
		cellAttributes.setCellAttribute("class", initialClassCellAttributes);
		cellAttributes.appendCellAttribute("class", value);
	}

	public Attributes getCellAttributes() {
		return cellAttributes;
	}

	/**
	 * @return Returns the attributes.
	 */
	public String getAttributes() {
		return attributesString;
	}

	/**
	 * @param attributes The attributes to set.
	 */
	public void setAttributes(String attributes) {
		this.attributesString = attributes;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		resetAttributes();
		return result;
	}

	protected void resetAttributes() {
		cellAttributes.reset();
		initialClassCellAttributes = null;
		attributesString = null;
	}

	/**
	 * Called on a Tag handler to release state. The page compiler guarantees that JSP page
	 * implementation objects will invoke this method on all tag handlers, but there may be multiple
	 * invocations on doStartTag and doEndTag in between.
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		resetAttributes();
	}
}