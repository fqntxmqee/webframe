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

import javax.servlet.jsp.PageContext;

import org.webframe.web.page.web.ValueListConfigBean;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.9 $ $Date: 2005/11/23 14:51:53 $
 */
public class TableInfo {

	public static final String		DEFAULT_ID	= "";

	private String						id				= DEFAULT_ID;

	/** The web address to post to if actions are desired. * */
	private String						url;

	/** The name of the ValueList. * */
	private String						name;

	/** Holds the included parameters. */
	private Map<String, Object>	parameters	= new HashMap<String, Object>();

	private ValueListConfigBean	config;

	private PageContext				pageContext;

	/** Default constructor. */
	public TableInfo(String id) {
		this.id = id;
	}

	/**
	 * Gets the base url that the links are build upon.
	 * 
	 * @return The base url that the links are build upon.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the base url that the links are build upon.
	 * 
	 * @param url The base url that the links are build upon.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return Returns the parameters.
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	// public String getParametersAsString()
	// {
	// return JspUtils.toUrlString(parameters);
	// }
	// public String getParametersAsString(List exclude)
	// {
	// return JspUtils.toUrlString(parameters, exclude);
	// }
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
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		if (id != null) {
			this.id = id;
		}
	}

	/**
	 * @return Returns the config.
	 */
	public ValueListConfigBean getConfig() {
		return config;
	}

	/**
	 * @return Returns the pageContext.
	 */
	public PageContext getPageContext() {
		return pageContext;
	}

	/**
	 * @param config The config to set.
	 */
	public void setConfig(ValueListConfigBean config) {
		this.config = config;
	}

	/**
	 * @param pageContext The pageContext to set.
	 */
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}
}