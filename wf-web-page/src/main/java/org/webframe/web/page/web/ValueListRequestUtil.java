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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.tag.ActionTag;

/**
 * This is a utility to assist usig the ValueList in a web enviroment.
 * org.webframe.web.page.web.ValueListRequestUtil
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.16 $ $Date: 2006/03/28 17:06:49 $
 */
public final class ValueListRequestUtil {

	/** Protect singleton */
	private ValueListRequestUtil() {
	}

	public static Map<String, Object> getRequestParameterMap(HttpServletRequest request) {
		return getRequestParameterMap(request, "");
	}

	public static Map<String, Object> getRequestParameterMap(Map<String, Object> requestParameterMap, String id) {
		if (requestParameterMap == null) {
			return new HashMap<String, Object>();
		}
		int lenghtOfId = (id == null) ? 0 : id.length();
		Map<String, Object> parameters = new HashMap<String, Object>(requestParameterMap.size());
		for (Iterator<String> keys = requestParameterMap.keySet().iterator(); keys.hasNext();) {
			String key = keys.next();
			String[] values;
			Object valuesObj = requestParameterMap.get(key);
			if (valuesObj instanceof String) {
				values = new String[]{
					(String) valuesObj};
			} else {
				values = (String[]) valuesObj;
			}
			Object value = ((values == null) ? null : (values.length == 1 ? (Object) values[0] : (Object) values));
			if (lenghtOfId > 0) {
				if (key.endsWith(id)) {
					parameters.put(key.substring(0, key.length() - lenghtOfId), value);
				}
			} else {
				parameters.put(key, value);
			}
		}
		return parameters;
	}

	/**
	 * @param request
	 * @param id
	 * @return A Map of string or String arrays.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestParameterMap(HttpServletRequest request, String id) {
		int lenghtOfId = (id == null) ? 0 : id.length();
		Map<String, Object> parameters = new HashMap<String, Object>(request.getParameterMap().size());
		for (Enumeration<String> keys = request.getParameterNames(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			String[] values = request.getParameterValues(key);
			Object value = ((values == null) ? null : (values.length == 1 ? (Object) values[0] : (Object) values));
			if (lenghtOfId > 0) {
				if (key.endsWith(id)) {
					parameters.put(key.substring(0, key.length() - lenghtOfId), value);
				}
			} else {
				parameters.put(key, value);
			}
		}
		return parameters;
	}

	/**
	 * Build the ValueListInfo from the request and parameters.
	 * 
	 * @param request The HttpServletRequest
	 * @return a new ValueListInfo.
	 */
	public static ValueListInfo buildValueListInfo(HttpServletRequest request) {
		return buildValueListInfo(request, "");
	}

	/**
	 * Build the ValueListInfo from the request for certain Table id.
	 * 
	 * @param request The HttpServletRequest
	 * @param id The tableId String.
	 * @return a new ValueListInfo.
	 */
	public static ValueListInfo buildValueListInfo(HttpServletRequest request, String id) {
		Map<String, Object> parameters = getRequestParameterMap(request, id);
		return builtValueListInfo(parameters);
	}

	/**
	 * Make from parameters ValueListInfo
	 * 
	 * @param parameters
	 * @return ValueListInfo
	 */
	private static ValueListInfo builtValueListInfo(Map<String, Object> parameters) {
		return new ValueListInfo(parameters);
	}

	// Note setIncludeParameters was refactor to getAllForwardParameters
	/**
	 * Setter for the parameters, that are forwarded in every url link in table. <ul><li><b># </b>-
	 * forward only control (like sortColumn, pagingPage, etc.) parameters of others tables in jsp
	 * <p>Automatically exclude params with the prefix of the ACTION_TEMP_PARAM_PREFIX. </p> </li>
	 * <li><b>$ </b>- forward only parameters of others tables in jsp that stats with prefix
	 * ACTION_TEMP_PARAM_PREFIX. </li> <li><b>partOfKey*</b> - forward parameteres that starts with
	 * prefix <b>partOfKey</b>.<p> Automatically are excluded keys start with
	 * <ul>ACTION_TEMP_PARAM_PREFIX and with</ul> <ul>DO_FOCUS+id of this table.</ul> </p> </li>
	 * <li><b>myForwardParameterOne|myForwardParameterTwo </b>- forward parameter from request if the
	 * name is as the same as <b>myForwardParameterOne</b> or <b>myForwardParameterTwo</b></li> </ul>
	 * <p> Note: ValueListInfo.DO_FOCUS param you cannot forward. It is forbidden for all
	 * posibilities. </p>
	 * 
	 * @param userForwardParameters The userForwardParameters properties.
	 * @param id TableId of the currrent value list table.
	 * @see org.webframe.web.page.web.tag.ActionTag#ACTION_TEMP_PARAM_PREFIX
	 * @see org.webframe.web.page.ValueListInfo#DO_FOCUS
	 */
	public static Map<String, String[]> getAllForwardParameters(PageContext pageContext, String userForwardParameters, String id) {
		Map<String, String[]> forwardedParams = new HashMap<String, String[]>();
		for (StringTokenizer st = new StringTokenizer(userForwardParameters, "|"); st.hasMoreTokens();) {
			String key = st.nextToken();
			if (key.equals("#")) {
				forwardedParams.putAll(getForwardParamsOfOtherTables(pageContext, id));
			} else {
				if (key.endsWith("*")) {
					key = key.substring(0, key.length() - 1);
					forwardedParams.putAll(getForwardParamsOfKey(pageContext, id, key));
				} else {
					if (key.equals("$")) {
						forwardedParams.putAll(getForwardActionTempParamsOfOtherTables(pageContext, id));
					} else {
						String[] values = pageContext.getRequest().getParameterValues(key);
						if (values != null && values.length > 0) {
							forwardedParams.put(key, values);
						}
					}
				}
			}
		}
		return forwardedParams;
	}

	/**
	 * @param pageContext
	 * @param id TableId
	 * @param prefix
	 * @return Map Map of forwarded parameters starts with prefix. Automatically are excluded
	 *         DO_FOCUS for this TableId and params with the prefix of the ACTION_TEMP_PARAM_PREFIX.
	 */
	private static Map<String, String[]> getForwardParamsOfKey(PageContext pageContext, String id, String prefix) {
		Map<String, String[]> paramsOfKey = new HashMap<String, String[]>();
		for (Enumeration<String> enumer = getParameterNames(pageContext); enumer.hasMoreElements();) {
			String requestParamName = enumer.nextElement();
			if (requestParamName.startsWith(prefix)
						&& (!requestParamName.startsWith(ValueListInfo.DO_FOCUS + id))
						&& (!requestParamName.startsWith(ActionTag.ACTION_TEMP_PARAM_PREFIX))) {
				String[] values = pageContext.getRequest().getParameterValues(requestParamName);
				if (values != null) {
					paramsOfKey.put(requestParamName, values);
				}
			}
		}
		return paramsOfKey;
	}

	/**
	 * @param pageContext
	 * @param id TableId
	 * @return Map Map of action's temp parameters of others tables in jsp page.
	 */
	private static Map<String, String[]> getForwardActionTempParamsOfOtherTables(PageContext pageContext, String id) {
		Map<String, String[]> actionTempParamsOfOthers = new HashMap<String, String[]>();
		for (Enumeration<String> enumer = getParameterNames(pageContext); enumer.hasMoreElements();) {
			String requestParamName = enumer.nextElement();
			if (requestParamName.startsWith(ActionTag.ACTION_TEMP_PARAM_PREFIX) && !requestParamName.endsWith(id)) {
				String[] values = pageContext.getRequest().getParameterValues(requestParamName);
				if (values != null) {
					actionTempParamsOfOthers.put(requestParamName, values);
				}
			}
		}
		return actionTempParamsOfOthers;
	}

	/**
	 * @param pageContext
	 * @param id TableId
	 * @return Map of table's control's parameters of other's tables in jsp page,automatically
	 *         exclude params with the prefix of the ACTION_TEMP_PARAM_PREFIX.
	 */
	private static Map<String, String[]> getForwardParamsOfOtherTables(PageContext pageContext, String id) {
		Map<String, String[]> otherTablesParams = new HashMap<String, String[]>();
		for (Enumeration<String> enumer = getParameterNames(pageContext); enumer.hasMoreElements();) {
			String requestParamName = (String) enumer.nextElement();
			if ((!requestParamName.endsWith(id) && !requestParamName.startsWith(ActionTag.ACTION_TEMP_PARAM_PREFIX))
						&& (requestParamName.startsWith(ValueListInfo.FOCUS_PARAM_PREFIX)
									|| requestParamName.startsWith(ValueListInfo.PAGING_PARAM_PREFIX) || requestParamName.startsWith(ValueListInfo.SORT_PARAM_PREFIX))) {
				String[] values = pageContext.getRequest().getParameterValues(requestParamName);
				if (values != null) {
					otherTablesParams.put(requestParamName, values);
				}
			}
		}
		return otherTablesParams;
	}

	@SuppressWarnings("unchecked")
	private static Enumeration<String> getParameterNames(PageContext pageContext) {
		return pageContext.getRequest().getParameterNames();
	}

	/**
	 * Return the name of parameter with ActionTag.ACTION_TEMP_PARAM_PREFIX Example: param "id"
	 * return "ACTid"
	 * 
	 * @param userParamName
	 * @return ActionTag.ACTION_TEMP_PARAM_PREFIX + userParamName
	 */
	public static String getActionTempParamName(String userParamName) {
		return ActionTag.ACTION_TEMP_PARAM_PREFIX + userParamName;
	}
}