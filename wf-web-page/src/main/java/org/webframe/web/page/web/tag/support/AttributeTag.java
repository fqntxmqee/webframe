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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.webframe.web.page.web.util.JspUtils;

/**
 * Tag Library that sets properties on a Attributable.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.5 $ $Date: 2005/11/23 14:37:38 $
 */
public class AttributeTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1673875144032138645L;

	/** The name of this property. */
	private String					name;

	/** The value of this property. */
	private String					value;

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		Attributeable parent = (Attributeable) JspUtils.getParent(this, Attributeable.class);
		if (value != null) {
			parent.setCellAttribute(name, value);
			value = null;
			return SKIP_BODY;
		} else {
			return EVAL_BODY_AGAIN;
		}
	}

	/**
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspException {
		Attributeable parent = (Attributeable) getParent();
		parent.setCellAttribute(name, bodyContent.getString());
		bodyContent.clearBody();
		return SKIP_BODY;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		reset();
		return result;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this property.
	 * 
	 * @param name The name of this property.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of this property.
	 * 
	 * @param value The value of this property.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	private void reset() {
		this.name = null;
		this.value = null;
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
		reset();
	}
}