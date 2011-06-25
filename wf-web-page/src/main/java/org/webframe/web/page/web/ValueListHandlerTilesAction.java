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
package org.webframe.web.page.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListHandler;
import org.webframe.web.page.ValueListInfo;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.4 $ $Date: 2004/12/13 20:32:35 $
 */
public class ValueListHandlerTilesAction extends ControllerSupport
{
	public static final String VALUE_LIST_NAME = "valueListName";
	public static final String VALUE_LIST_REQUEST_NAME = "valueListRequestName";

	/**
	 * @see org.apache.struts.tiles.Controller#perform(org.apache.struts.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
	 */
	public void perform(ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws ServletException, IOException
	{
		String name = (String) componentContext.getAttribute(VALUE_LIST_NAME);
		String requestName = (String) componentContext.getAttribute(VALUE_LIST_REQUEST_NAME);

		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		ValueListHandler vlh = (ValueListHandler) context.getBean("valueListHandler", ValueListHandler.class);

		ValueListInfo info = ValueListRequestUtil.buildValueListInfo(request);
		ValueList valueList = vlh.getValueList(name, info);

		request.setAttribute(requestName, valueList);
	}

}