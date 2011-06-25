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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.web.util.JspUtils;

/**
 * Generate a select box from a ValueList <br>
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.8 $ $Date: 2005/11/23 15:02:16 $
 */
public class DefaultSelectTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6260909463717688895L;

	private static final Log	LOGGER				= LogFactory.getLog(DefaultSelectTag.class);

	private String					name;

	private String					attributes;

	private String					value;

	private String					text;

	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		ValueList valueList = rootTag.getValueList();
		StringBuffer sb = new StringBuffer();
		sb.append("<select name=").append("'").append(name).append("'").append(attributes).append(">");
		if (bodyContent != null && bodyContent.getString() != null) {
			sb.append(bodyContent.getString());
		}
		try {
			String[] svalues = pageContext.getRequest().getParameterValues(name);
			List<String> values = Collections.emptyList();
			if (svalues != null) {
				values = Arrays.asList(svalues);
			}
			for (Iterator<Object> iter = valueList.getList().iterator(); iter.hasNext();) {
				Object bean = iter.next();
				String value = BeanUtils.getProperty(bean, this.value);
				sb.append("<option ");
				if (values.contains(value)) {
					sb.append("selected='true' ");
				}
				sb.append("value='").append(value).append("'>");
				sb.append(BeanUtils.getProperty(bean, text));
				sb.append("</option>");
			}
		} catch (Exception e) {
			LOGGER.error("DefaultSelectTag.doEndTag() exception...", e);
			throw new JspException(e);
		}
		sb.append("</select>");
		JspUtils.write(pageContext, sb.toString());
		release();
		return EVAL_PAGE;
	}

	/**
	 * @return Returns the attributes.
	 */
	public String getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes The attributes to set.
	 */
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	private void reset() {
		this.attributes = null;
		this.name = null;
		this.text = null;
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