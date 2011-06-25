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

package org.webframe.web.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This bean holds all the information needed to retrieve a ValueList. This information is needed to
 * allow for server side sorting, paging and focusing.
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.20 $ $Date: 2006/03/29 19:47:49 $
 */
public class ValueListInfo implements Serializable, PagingInfo {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long		serialVersionUID				= 3257572797588191543L;

	/** The name of the ValueList requested. * */
	public static final String		VALUE_LIST_NAME				= "valueListName";

	/** Constant for an descending order. */
	public static final Integer	DESCENDING						= new Integer(-1);

	/** Constant for an ascending order. */
	public static final Integer	ASCENDING						= new Integer(1);

	/** Constant for an descending order. */
	private static final String	DESCENDING_STRING				= "desc";

	/** Constant for an ascending order. */
	private static final String	ASCENDING_STRING				= "asc";

	/**
	 * General prefix for paging's parameters.
	 */
	public static final String		PAGING_PARAM_PREFIX			= "paging";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		PAGING_PAGE						= PAGING_PARAM_PREFIX + "Page";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		PAGING_NUMBER_PER				= PAGING_PARAM_PREFIX + "NumberPer";

	/**
	 * General prefix for sorting's parameters.
	 */
	public static final String		SORT_PARAM_PREFIX				= "sort";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		SORT_COLUMN						= SORT_PARAM_PREFIX + "Column";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		SORT_DIRECTION					= SORT_PARAM_PREFIX + "Direction";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		DO_FOCUS							= "doFocus";

	/**
	 * General prefix for focus's parameters, DO_FOCUS has different
	 */
	public static final String		FOCUS_PARAM_PREFIX			= "focus";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		FOCUS_VALUE						= FOCUS_PARAM_PREFIX + "Value";

	/****************************************************************************************************************************************
	 * The string name of the attribute that should be used in Map backed transports, such as a
	 * Request Object.
	 ***************************************************************************************************************************************/
	public static final String		FOCUS_PROPERTY					= FOCUS_PARAM_PREFIX + "Property";

	/**
	 * The number of row in the table of the generated html page, in which is focus value of the
	 * focusProperty. Focus property could be hidden in the generated page.
	 */
	private int							focusedRowNumberInTable		= -1;

	/** Focus status. */
	private byte						focusStatus						= FOCUS_STATUS_NOT_DEFINED;

	/** Focus status when is not used. */
	public static final byte		FOCUS_STATUS_NOT_DEFINED	= 0;

	/** Focus value was founded. */
	public static final byte		FOCUS_FOUND						= 1;

	/** Focus value was not founded. */
	public static final byte		FOCUS_NOT_FOUND				= 2;

	/** Focusing is not possible, because in ResultSet are too many items - rows. */
	public static final byte		FOCUS_TOO_MANY_ITEMS			= 4;

	/** Holds the filters. */
	private Map<String, Object>	filters							= null;

	/** Total number of pages available. */
	private int							totalNumberOfEntries			= 0;

	/**
	 * Enable or disable valuelist paging. This setting should be used in the adapter in conjunction
	 * with DO_PAGE setting.
	 */
	// private boolean pagingEnabled = true;
	/** Logger for logging warnings etc. */
	private static final Log		LOGGER							= LogFactory.getLog(ValueListInfo.class);

	/**
	 * Default constructor.
	 */
	public ValueListInfo() {
		filters = new HashMap<String, Object>();
	}

	/**
	 * Default constructor.
	 * 
	 * @param parameters The filters/parameters to initialize.
	 */
	public ValueListInfo(Map<String, Object> parameters) {
		filters = new HashMap<String, Object>(parameters);
		String sortColumn = getFilterParameterAsString(SORT_COLUMN);
		if (sortColumn != null) {
			setPrimarySortColumn(sortColumn);
		}
		String direction = getFilterParameterAsString(SORT_DIRECTION);
		if (direction != null) {
			setPrimarySortDirection(ASCENDING_STRING.equals(direction) || "1".equals(direction) ? ASCENDING : DESCENDING);
		}
	}

	/**
	 * Creates a new ValueListInfo that contains sorting information
	 * 
	 * @param primaryColumn The column to sort by
	 * @param primaryDirection The direction to sort the <CODE>ValueList</CODE> by
	 */
	public ValueListInfo(String primaryColumn, Integer primaryDirection) {
		this(primaryColumn, primaryDirection, new HashMap<String, Object>());
	}

	/**
	 * Creates a new ValueListInfo that contains sorting information
	 * 
	 * @param primaryColumn The column to sort by
	 * @param primaryDirection The direction to sort the <CODE>ValueList</CODE> by
	 * @param filters The filters of search criteria.
	 */
	public ValueListInfo(String primaryColumn, Integer primaryDirection, Map<String, Object> filters) {
		// Set filters first, because the following methods need it.
		this.filters = (filters == null) ? new HashMap<String, Object>() : filters;
		setPrimarySortColumn(primaryColumn);
		setPrimarySortDirection(primaryDirection);
	}

	/**
	 * Getter for property filters.
	 * 
	 * @return Value of property filters.
	 */
	public Map<String, Object> getFilters() {
		return filters;
	}

	/**
	 * Returns an array of column (property) names.
	 * 
	 * @return a String array of column (property) names. null if no sorting information exists.
	 */
	public String getSortingColumn() {
		return getFilterParameterAsString(SORT_COLUMN);
	}

	public void setPrimarySortColumn(String column) {
		if (column == null) {
			filters.remove(SORT_COLUMN);
		} else {
			filters.put(SORT_COLUMN, column);
		}
	}

	/**
	 * Returns an array of directions.
	 * 
	 * @return an Integer array of directions. null if no sorting information exists.
	 */
	public Integer getSortingDirection() {
		String direction = getFilterParameterAsString(SORT_DIRECTION);
		return ((ASCENDING_STRING.equals(direction) || "1".equals(direction))
					? ValueListInfo.ASCENDING
					: ValueListInfo.DESCENDING);
	}

	public void setPrimarySortDirection(Integer direction) {
		if (direction == null) {
			filters.remove(SORT_DIRECTION);
		} else {
			filters.put(SORT_DIRECTION, ((direction.intValue() > 0) ? ASCENDING_STRING : DESCENDING_STRING));
		}
	}

	/**
	 * Getter for the curent page to display.
	 * 
	 * @return the curent page to display.
	 */
	public int getPagingPage() {
		String page = getFilterParameterAsString(PAGING_PAGE);
		try {
			return (page == null || page.length() == 0) ? 1 : Integer.parseInt(page);
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	/**
	 * Getter for the total number of pages.
	 * 
	 * @return the total number of pages.
	 */
	public int getTotalNumberOfPages() {
		return (((totalNumberOfEntries - 1) / getPagingNumberPer()) + 1);
	}

	/**
	 * Getter for the total number VOs.
	 * 
	 * @return the total number of VOs.
	 */
	public int getTotalNumberOfEntries() {
		return totalNumberOfEntries;
	}

	/**
	 * Getter for the number of VOs per page. Integer.MAX_VALUE if it is not set.
	 * 
	 * @return the number of VOs per page.
	 */
	public int getPagingNumberPer() {
		String number = getFilterParameterAsString(PAGING_NUMBER_PER);
		try {
			int count = (number == null || number.length() == 0) ? Integer.MAX_VALUE : Integer.parseInt(number);
			if (count > 0) {
				return count;
			} else {
				return Integer.MAX_VALUE;
			}
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * Sets the total number of items that meet the filter.
	 * 
	 * @param totalNumberOfEntries The total number of items that meet the filter.
	 */
	public void setTotalNumberOfEntries(int totalNumberOfEntries) {
		this.totalNumberOfEntries = totalNumberOfEntries;
	}

	/**
	 * The map to build the query.
	 * 
	 * @param filters Map to build the query
	 */
	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	/**
	 * Sets the number of items per page.
	 * 
	 * @param numberPerPage The number of line items per page.
	 */
	public void setPagingNumberPer(int numberPerPage) {
		filters.put(PAGING_NUMBER_PER, String.valueOf(numberPerPage));
	}

	/**
	 * Sets the current page number.
	 * 
	 * @param pageNumber The current page number.
	 */
	public void setPagingPage(int pageNumber) {
		filters.put(PAGING_PAGE, String.valueOf(pageNumber));
	}

	/**
	 * @return Returns the focusValue.
	 */
	public String getFocusValue() {
		return getFilterParameterAsString(FOCUS_VALUE);
	}

	/**
	 * @param focusValue The focusValue to set.
	 */
	public void setFocusValue(String focusValue) {
		if (focusValue == null) {
			filters.remove(FOCUS_VALUE);
		} else {
			filters.put(FOCUS_VALUE, focusValue);
		}
	}

	/**
	 * @return Returns the focusProperty.
	 */
	public String getFocusProperty() {
		return getFilterParameterAsString(FOCUS_PROPERTY);
	}

	/**
	 * Set focusProperty, if null, remove FOCUS_PROPERTY AND FOCUS_VALUE
	 * 
	 * @param focusProperty The focusProperty to set.
	 */
	public void setFocusProperty(String focusProperty) {
		if ((focusProperty == null || focusProperty.length() == 0)) {
			filters.remove(FOCUS_PROPERTY);
			filters.remove(FOCUS_VALUE);
		} else {
			filters.put(FOCUS_PROPERTY, focusProperty);
		}
	}

	/**
	 * @return true if getFocusProperty is not null
	 */
	public boolean isFocusEnabled() {
		return (getFocusProperty() != null);
	}

	/**
	 * @return true if is set DoFocus true
	 */
	public boolean isDoFocus() {
		String doFocus = getFilterParameterAsString(DO_FOCUS);
		try {
			return (doFocus != null) && (doFocus.length() > 0) && Boolean.valueOf(doFocus).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Used for generating links, if any errors found, doFocus is set to false for next retrieving of
	 * the valueList.
	 * 
	 * @return boolean
	 */
	public boolean isDoFocusAgain() {
		return isDoFocus() && (getFocusStatus() == FOCUS_FOUND);
	}

	/**
	 * @see org.webframe.web.page.PagingInfo#setFocusSucces(boolean)
	 */
	public void setDoFocus(boolean enabled) {
		filters.put(DO_FOCUS, String.valueOf(enabled));
		focusedRowNumberInTable = -1;
	}

	/**
	 * Set the certain row in table to be focused.
	 * 
	 * @param absolutPositionInResultSet
	 */
	public void setFocusedRowNumberInTable(int absolutPositionInResultSet) {
		focusedRowNumberInTable = absolutPositionInResultSet % getPagingNumberPer();
	}

	/**
	 * @return Returns the focusedRowNumberInTable.
	 */
	public int getFocusedRowNumberInTable() {
		if (getFocusStatus() == FOCUS_FOUND) {
			return focusedRowNumberInTable;
		} else {
			return -1;
		}
	}

	/** The resultSetRowNumber starts from 0. */
	public void setPagingPageFromRowNumber(int resultSetRowNumber) {
		setPagingPage(1 + Math.round(resultSetRowNumber / getPagingNumberPer()));
	}

	/**
	 * Acceptable values: FOUND, NOT_FOUND, TOO_MANY_ITEMS
	 */
	public void setFocusStatus(byte status) {
		focusStatus = status;
	}

	/**
	 * @return Returns the focusStatus.
	 */
	public byte getFocusStatus() {
		return focusStatus;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[filters='"
					+ getFilters()
					+ ", totalNumberOfEntries='"
					+ totalNumberOfEntries
					+ "', focusedRowNumberInTable='"
					+ focusedRowNumberInTable
					+ "',focusStatus="
					+ focusStatus
					+ "']";
	}

	public void resetSorting() {
		List<String> keys = new ArrayList<String>(filters.keySet());
		for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (key.startsWith(SORT_COLUMN) || key.startsWith(SORT_DIRECTION)) {
				filters.remove(key);
			}
		}
	}

	/**
	 * Extracts a value with the given key from filters and converts it to a String. If it isn't a
	 * String a warning is logged and null is returned.
	 * 
	 * @param key
	 * @return The value stored under the key in filters or null if not present or not a String.
	 */
	protected String getFilterParameterAsString(String key) {
		Object parameter = filters.get(key);
		if (parameter != null) {
			if (parameter instanceof String) {
				return (String) parameter;
			} else {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("Parameters contain the parameter '"
								+ key
								+ "' but it isn't String, as expected. It is a "
								+ parameter.getClass()
								+ " with the value "
								+ parameter, new ClassCastException());
				}
			}
		}
		return null;
	}
}