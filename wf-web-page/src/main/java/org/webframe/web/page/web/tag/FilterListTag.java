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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.util.JspUtils;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.11 $ $Date: 2005/11/23 15:00:33 $
 */
public class FilterListTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -4641065838876863482L;

	private static final String	DEFAULT_TOKEN		= "|";

	private String						token					= DEFAULT_TOKEN;

	private String						key;

	private String						format;

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
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspException {
		BodyContent body = getBodyContent();
		if (body != null) {
			List<String> ignoreList = new ArrayList<String>();
			ignoreList.add(getKey());
			TableInfo tableInfo = rootTag.getTableInfo();
			// Remove the page. Bug [1047622]
			parameters.remove(ValueListInfo.PAGING_PAGE + tableInfo.getId());
			String content = body.getString().trim();
			String valueInRequest = pageContext.getRequest().getParameter(getKey() + tableInfo.getId());
			JspUtils.writePrevious(pageContext, "<table class=\"filters\" width=\"100%\"><tr>");
			for (StringTokenizer st = new StringTokenizer(content, getToken()); st.hasMoreTokens();) {
				String token = st.nextToken();
				String value = (format == null) ? token : MessageFormat.format(format, new Object[]{
					token});
				boolean selected = value.equals(valueInRequest);
				parameters.put(getKey() + tableInfo.getId(), value);
				JspUtils.writePrevious(pageContext, (selected ? "<th>" : "<td>")
							+ "<a href=\""
							+ tableInfo.getUrl()
							+ rootTag.getConfig().getLinkEncoder().encode(pageContext, parameters)
							+ "\">");
				JspUtils.writePrevious(pageContext, token);
				JspUtils.writePrevious(pageContext, "</a>" + (selected ? "</th>" : "</td>"));
			}
			JspUtils.writePrevious(pageContext, "</tr></table>");
		}
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
	 * @return Returns the key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return Returns the token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token The token to set.
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	private void reset() {
		this.rootTag = null;
		this.format = null;
		this.key = null;
		this.parameters = null;
		this.token = DEFAULT_TOKEN;
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