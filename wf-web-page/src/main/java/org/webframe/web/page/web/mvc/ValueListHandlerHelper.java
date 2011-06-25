/**
 * Copyright (c) 2003 held jointly by the individual authors. This library is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details. You should have received a copy of the GNU Lesser
 * General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA. > http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.web.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListHandler;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListRequestUtil;
import org.webframe.web.page.web.tag.TableInfo;

/**
 * This package is used in mvc controlers to better handle with the ValueListHandler. It allow you
 * to <li>backup ValueList <b>info </b> to the session before redirect!</li> <li>modify paging
 * possition, focusing property and values, sorting order, etc.</li> <h5>Usage</h5> Please, before
 * use, set a ValueListHandler property with a reference to your valueListHandler!</h4>
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.6 $ $Date: 2005/11/23 14:19:45 $
 */
public class ValueListHandlerHelper {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER				= LogFactory.getLog(ValueListHandlerHelper.class);

	private ValueListHandler	_valueListHandler	= null;

	/**
	 * @param valueListHandler The valueListHandler to set.
	 */
	public void setValueListHandler(ValueListHandler valueListHandler) {
		_valueListHandler = valueListHandler;
	}

	/**
	 * Get a bean of the ValueListHandler.
	 * 
	 * @return ValueListHandler
	 */
	public ValueListHandler getValueListHandler() {
		return _valueListHandler;
	}

	/**
	 * Retrieve valueList from ValueListHadler bean.
	 * 
	 * @param name the name of the Adapter
	 * @param info
	 * @return ValueList
	 */
	public ValueList getValueList(String name, ValueListInfo info) {
		return getValueListHandler().getValueList(name, info);
	}

	/**
	 * To get ValueListInfo it use the TableInfo.DEFAULT_ID as the tableId.
	 * 
	 * @see ValueListHandlerHelper#getValueListInfo(HttpServletRequest, String)
	 * @see TableInfo#DEFAULT_ID
	 * @param request
	 * @return ValueListInfo
	 */
	public ValueListInfo getValueListInfo(HttpServletRequest request) {
		LOGGER.warn("Gettting a valueListInfo for a table with the default id!");
		return getValueListInfo(request, TableInfo.DEFAULT_ID);
	}

	/**
	 * Return ValueListInfo from a <b>request </b> (which is preferr to session) or a session or new
	 * ValueListInfo(). <li>If request contains any parameters, which belongs to a table with the
	 * <b>tableId </b>, return the ValueListInfo from the <b>request. </b> </li> <li>Otherwise if the
	 * session <b>contains attribute tableId </b>, then return it from the <b>session </b>.</li>
	 * <li>If the request or the session <b>dosn't </b> contain the ValueListInfo, return <b>new </b>
	 * ValueListInfo.</li>
	 * 
	 * @see ValueListHandlerHelper#getValueListInfo(HttpServletRequest)
	 * @param request
	 * @param tableId
	 * @return ValueListInfo
	 */
	public ValueListInfo getValueListInfo(HttpServletRequest request, String tableId) {
		if (request == null) {
			final String message = "getValueListInfo - request is null!";
			LOGGER.error(message);
			throw new NullPointerException(message);
		}
		if (tableId == null) {
			LOGGER.error("TableId is null!");
			tableId = TableInfo.DEFAULT_ID;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start to getting ValuelistInfo for the tableId '" + tableId + "'.");
		}
		Map<String, Object> requestParamsMap = ValueListRequestUtil.getRequestParameterMap(request, tableId);
		ValueListInfo info = null;
		if (requestParamsMap.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Try to get backup of an info from the session for the tableId '" + tableId + "'.");
			}
			HttpSession session = request.getSession();
			if (session != null) {
				info = (ValueListInfo) session.getAttribute(tableId);
			} else {
				LOGGER.warn("ValueListInfo for tableId '"
							+ tableId
							+ "'  was not found in the sesion due to session is null!");
			}
			if (LOGGER.isDebugEnabled()) {
				if (info == null) {
					LOGGER.debug("Backup of the ValueListInfo for the tableId '"
								+ tableId
								+ "'was NOT found in the session.");
				} else {
					LOGGER.debug("Backup of the ValueListInfo for the tableId '" + tableId + "' was FOUND in the session");
				}
			}
		} else {
			info = ValueListRequestUtil.buildValueListInfo(request, tableId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("ValueListInfo for the tableId '" + tableId + "' was build from the request's params.");
			}
		}
		if (info == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Creating a default ValueListInfo for the tableId '" + tableId + "'");
			}
			info = new ValueListInfo();
		}
		return info;
	}

	/**
	 * Do 2 things: <li>Backup the ValueList <b>Info to the session </b> with the attribute's name
	 * <b>tableId </b> and</li> <li>store Value <b>List to the request </b> with the attribute key's
	 * name <b>valueListName </b></li>
	 * 
	 * @see ValueListHandlerHelper#backupInfoFor(HttpServletRequest, ValueListInfo, String)
	 * @see ValueListHandlerHelper#setValueListTo(HttpServletRequest, ValueList, String)
	 * @param request
	 * @param valueList null -> info will be removed from session and storing will be skipped.
	 * @param valueListName the name used in the &lt;vlh:root value="valueListName" ... to retrieve
	 * @param tableId unique id in the session
	 */
	public void backupAndSet(HttpServletRequest request, ValueList valueList, String valueListName, String tableId) {
		backupInfoFor(request, valueList == null ? null : valueList.getValueListInfo(), tableId);
		setValueListTo(request, valueList, valueListName);
	}

	/**
	 * Store only ValueList to request as an attribute with the name valueListName.
	 * 
	 * @param request
	 * @param valueList ValueList to store
	 * @param valueListName request attribute's
	 */
	public void setValueListTo(HttpServletRequest request, ValueList valueList, String valueListName) {
		if (valueListName != null && valueListName.length() > 0) {
			request.setAttribute(valueListName, valueList);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("ValueList '" + valueListName + "' is stored in the request.");
			}
		} else {
			LOGGER.error("Skiped storing of the ValueList to the request, valueListName is null or empty!");
		}
	}

	/**
	 * Save ValueListInfo to the session with key of TableInfo.DEFAULT_ID. You should prevent of use
	 * this method.
	 * 
	 * @see TableInfo#DEFAULT_ID
	 * @see ValueListHandlerHelper#backupInfoFor(HttpServletRequest, ValueListInfo, String)
	 * @param request HttpServletRequest
	 * @param info ValueListInfo
	 */
	public void backupInfoFor(HttpServletRequest request, ValueListInfo info) {
		LOGGER.warn("Backing Up with the default table id ");
		backupInfoFor(request, info, TableInfo.DEFAULT_ID);
	}

	/**
	 * Save ValueListInfo to session with key of tableId if info is null, remove the tableId
	 * attribute from session.
	 * 
	 * @param request HttpServletRequest
	 * @param info ValueListInfo to back up to session
	 * @param tableId String session's attributes name
	 * @see TableInfo#DEFAULT_ID <h4> during accesing session, we are not using any
	 *      synchronization.</h4>
	 */
	public void backupInfoFor(HttpServletRequest request, ValueListInfo info, String tableId) {
		if (tableId != null && tableId.length() > 0) {
			if (info == null) {
				request.getSession().removeAttribute(tableId);
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("ValueListInfo to back up is null! ValueListInfo for the tableId '"
								+ tableId
								+ "' was removed from the session.");
				}
			} else {
				request.getSession().setAttribute(tableId, info);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("ValueListInfo for tableId '" + tableId + "' was saved in session.");
				}
			}
		} else {
			throw new NullPointerException("The session unique attribute tableId is null or empty, skipped backUp of valueListInfo into the session!");
		}
	}

	/**
	 * Return the name of parameter with ActionTag.ACTION_TEMP_PARAM_PREFIX Example: param <b>id </b>
	 * return <b>ACTid </b>
	 * 
	 * @see org.webframe.web.page.web.tag.ActionTag#ACTION_TEMP_PARAM_PREFIX
	 * @param request
	 * @param name
	 * @return String
	 */
	public String getActionTempParam(HttpServletRequest request, String name) {
		return request.getParameter(ValueListRequestUtil.getActionTempParamName(name));
	}

	/**
	 * Return the name of parameter without ActionTag.ACTION_TEMP_PARAM_PREFIX Example: param <b>id
	 * </b> return <b>id </b>
	 * 
	 * @see org.webframe.web.page.web.tag.ActionTag#ACTION_TEMP_PARAM_PREFIX
	 * @param request
	 * @param name
	 * @return String
	 */
	public String getActionParam(HttpServletRequest request, String name) {
		return request.getParameter(name);
	}

	/**
	 * If delete first and at that time also the last entry on the last page, you sets actual last
	 * page! You should call this method in a action after delete is done.
	 * 
	 * @param info ValueListInfo
	 * @return int Last page, or last page -1 of last retrieved valueListInfo.
	 * @see ValueListInfo#getTotalNumberOfEntries()
	 * @see ValueListInfo#getPagingNumberPer()
	 * @see ValueListInfo#getPagingPer()
	 */
	public int getLastPageAfterDelete(ValueListInfo info) {
		int lastPage = info.getPagingPage();
		int lastCount = info.getTotalNumberOfEntries() % info.getPagingNumberPer();
		if (lastCount == 1 && lastPage > 1) {
			lastPage--;
		}
		return lastPage;
	}
}