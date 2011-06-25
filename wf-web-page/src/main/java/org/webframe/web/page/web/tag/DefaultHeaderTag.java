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
 * This tag creates a table. It is ment to be used along with
 * 
 * @todo Document this tag.
 * @author Matthew L. Wilson
 * @version $Revision: 1.14 $ $Date: 2005/11/23 15:02:16 $
 */
public class DefaultHeaderTag extends DefaultRowTag {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 309248156024291671L;

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		init();
		return EVAL_BODY_AGAIN;
	}

	/**
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspException {
		return SKIP_BODY;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 * @todo figure out why release is not working.
	 */
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		TableInfo tableInfo = getRootTag().getTableInfo();
		renderHeaderRow(sb, getColumns(), tableInfo, getRootTag().getValueList().getValueListInfo(),
			tableInfo.getParameters());
		JspUtils.writePrevious(pageContext, sb.toString());
		return super.doEndTag();
	}
}