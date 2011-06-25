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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.commons.beanutils.PropertyUtils;
import org.webframe.web.page.web.ValueListConfigBean;
import org.webframe.web.page.web.tag.support.ColumnInfo;
import org.webframe.web.page.web.util.JspUtils;

/**
 * @ author Matthew L. Wilson, Andrej Zachar
 * 
 * @version $Revision: 1.10 $ $Date: 2006/01/06 10:53:54 $
 */
public class DefaultColumnCheckBoxTag extends BaseColumnTag {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6124838661495906339L;

	private String					name;

	private String					property;

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		ValueListConfigBean config = rootTag.getConfig();
		DefaultRowTag rowTag = (DefaultRowTag) JspUtils.getParent(this, DefaultRowTag.class);
		appendClassCellAttribute(rowTag.getRowStyleClass());
		Locale locale = config.getLocaleResolver().resolveLocale((HttpServletRequest) pageContext.getRequest());
		if (rowTag.getCurrentRowNumber() == 0) {
			String titleKey = getTitleKey();
			String label = (titleKey == null) ? getTitle() : config.getMessageSource().getMessage(titleKey, null,
				titleKey, locale);
			StringBuffer header = new StringBuffer(512);
			if (label != null) {
				header.append(label);
			}
			header.append(
				"<input type=\"checkbox\" onclick=\"for(i=0; i < this.form.elements.length; i++) {if (this.form.elements[i].name=='")
				.append(getName())
				.append("') {this.form.elements[i].checked = this.checked;}}\"/>");
			ColumnInfo columnInfo = new ColumnInfo(config.getDisplayHelper().help(pageContext, header.toString()), property, null, getAttributes());
			// Process toolTip if any
			// toolTip or toolTipKey is set => get the string and put it into the ColumnInfo
			String toolTipKey = getToolTipKey();
			columnInfo.setToolTip((toolTipKey == null) ? getToolTip() : config.getMessageSource().getMessage(toolTipKey,
				null, toolTipKey, locale));
			rowTag.addColumnInfo(columnInfo);
		}
		Object bean = pageContext.getAttribute(rowTag.getBeanName());
		Object value = "na";
		try {
			value = PropertyUtils.getProperty(bean, property);
		} catch (Exception e) {
		}
		StringBuffer sb = new StringBuffer();
		sb.append(rowTag.getDisplayProvider().getCellPreProcess(getCellAttributes()));
		BodyContent bodyContent = getBodyContent();
		if (bodyContent != null && bodyContent.getString() != null && bodyContent.getString().length() > 0) {
			sb.append(bodyContent.getString());
			bodyContent.clearBody();
		} else {
			sb.append("<input type=\"checkbox\" name=\"").append(name).append("\" value=\"").append(value).append("\"/>");
		}
		sb.append(rowTag.getDisplayProvider().getCellPostProcess());
		JspUtils.write(pageContext, sb.toString());
		release();
		return EVAL_PAGE;
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return this.property;
	}

	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	private void reset() {
		this.name = null;
		this.property = null;
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