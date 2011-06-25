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

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;

import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.util.JspUtils;

/**
 * @todo Document this tag.
 * @author Matthew L. Wilson
 * @version $Revision: 1.6 $ $Date: 2005/11/23 15:02:16 $
 */
public abstract class DefaultColumnsTag extends ConfigurableTag {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1733232213830678469L;

	/** Commons logger. */
	// private static final Log LOGGER = LogFactory.getLog(DefaultColumnsTag.class);
	protected Integer					defaultSort;

	/** Holds the included properties. */
	protected Collection<String>	include				= new ArrayList<String>();

	/** Holds the excluded properties. */
	protected Collection<String>	exclude				= new ArrayList<String>();

	/**
	 * Sets the defaultSort property.
	 * 
	 * @param value Valid values are "asc" and "desc".
	 */
	public void setSortable(String value) {
		if ("asc".equals(value)) {
			defaultSort = ValueListInfo.ASCENDING;
		} else if ("desc".equals(value)) {
			defaultSort = ValueListInfo.DESCENDING;
		}
	}

	/**
	 * Setter for the included properties
	 * 
	 * @param included The included properties
	 */
	public void setInclude(String included) {
		include = JspUtils.toCollection(included, "|");
	}

	/**
	 * Setter for the excluded properties
	 * 
	 * @param excluded The excluded properties
	 */
	public void setExclude(String excluded) {
		exclude = JspUtils.toCollection(excluded, "|");
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		reset();
		return result;
	}

	private void reset() {
		this.defaultSort = null;
		this.exclude.clear();
		this.include.clear();
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