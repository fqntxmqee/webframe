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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.webframe.web.page.web.tag.support.Attributeable;
import org.webframe.web.page.web.util.JspUtils;

/**
 * @deprecated use action tag
 * @author Matthew L. Wilson
 * @version $Revision: 1.8 $ $Date: 2005/11/23 15:00:21 $
 */
public class FilterTag extends BodyTagSupport implements Attributeable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1487751363780257459L;

	private String						url;

	private Map<String, Object>	parameters;

	private ValueListSpaceTag		rootTag;

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		parameters = new HashMap<String, Object>(rootTag.getTableInfo().getParameters());
		return super.doStartTag();
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		rootTag.getValueList();
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"").append(url);
		sb.append(rootTag.getConfig().getLinkEncoder().encode(pageContext, parameters));
		sb.append("\">");
		sb.append(getBodyContent().getString());
		sb.append("</a>");
		JspUtils.write(pageContext, sb.toString());
		release();
		return EVAL_PAGE;
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.Attributeable#setCellAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public void setCellAttribute(String name, String value) {
		parameters.put(name, value);
	}

	/**
	 * This tag will not append cell atributes, while they are used as link parameters !?
	 * 
	 * @todo mathew removed this tag!
	 * @see org.webframe.web.page.web.tag.support.Attributeable#setCellAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public void appendClassCellAttribute(String name, String value) {
		parameters.put(name, value);
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	private void reset() {
		this.rootTag = null;
		this.parameters = null;
		this.url = null;
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