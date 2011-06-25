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

import org.webframe.web.page.web.util.JspUtils;

/**
 * Base class for columnTags
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.5 $ $Date: 2006/01/06 10:53:24 $
 */
public class BaseColumnTag extends ConfigurableTag {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2810952083194972957L;

	private DefaultRowTag		rowTag;

	/** The value to display in the column header. */
	private String					title;

	private String					titleKey;

	/**
	 * The value to display in the column header tooltip. (In html rendered as the attribute 'title'
	 * of &lt;th&gt;).
	 */
	private String					toolTip;

	/**
	 * A key of the localized message to be displayed in the column header tooltip. (In html rendered
	 * as the attribute 'title' of &lt;th&gt;).
	 */
	private String					toolTipKey;

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the value to display in the column header.
	 * 
	 * @param title The value to display in the column header.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the titleKey.
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * @param titleKey The titleKey to set.
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/**
	 * The value to display in the column header tooltip. It's shown when a mouse pointer is over the
	 * title. It's useful when you don't want a long title but still you'd like to provide to a user
	 * a more detailed info about the column. (In html rendered as the attribute 'title' of
	 * &lt;th&gt;).
	 * 
	 * @return The value to display in the column header tooltip.
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * The value to display in the column header tooltip. (In html rendered as the attribute 'title'
	 * of &lt;th&gt;).
	 * 
	 * @param toolTip The value to display in the column header tooltip.
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * Return the key of the localized message to be displayed in the column header tooltip. (In html
	 * rendered as the attribute 'title' of &lt;th&gt;).
	 * 
	 * @return The key of the localized message to be displayed in the column header tooltip.
	 * @see org.springframework.context.support.ResourceBundleMessageSource
	 */
	public String getToolTipKey() {
		return toolTipKey;
	}

	/**
	 * Set the key of the localized message to be displayed in the column header tooltip. (In html
	 * rendered as the attribute 'title' of &lt;th&gt;).
	 * 
	 * @param toolTipKey The key of the localized message to be displayed in the column header
	 *           tooltip.
	 * @see org.springframework.context.support.ResourceBundleMessageSource
	 */
	public void setToolTipKey(String toolTipKey) {
		this.toolTipKey = toolTipKey;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		reset();
		return result;
	}

	public DefaultRowTag getRowTag() throws JspException {
		if (rowTag == null) {
			rowTag = (DefaultRowTag) JspUtils.getParent(this, DefaultRowTag.class);
		}
		return rowTag;
	}

	private void reset() {
		this.rowTag = null;
		this.title = null;
		this.titleKey = null;
		this.toolTip = null;
		this.toolTipKey = null;
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