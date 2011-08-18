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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This default implementation of the ValueListHandler service used Jakartas digester to create a
 * Configuration object.
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.15 $ $Date: 2006/03/29 19:47:49 $
 */
public class DefaultValueListHandlerImpl implements ValueListHandler {

	/**
	 * Compares two beans.
	 * 
	 * @author Matthew L. Wilson
	 * @version $Revision: 1.15 $ $Date: 2006/03/29 19:47:49 $
	 */
	public static class BeanComparator implements Comparator<Object> {

		/** The direction to sort. */
		private int		direction	= -1;

		/** The method to sort upon. */
		private String	method		= null;

		/**
		 * Default constructor.
		 * 
		 * @param pmethod The method to sort upon.
		 * @param pdirection The direction to sort.
		 */
		public BeanComparator(String pmethod, int pdirection) {
			this.method = pmethod;
			this.direction = pdirection;
		}

		/**
		 * @see java.util.Comparator.compare(Object, Object)
		 */
		@SuppressWarnings("unchecked")
		public int compare(Object obj1, Object obj2) {
			try {
				obj1 = PropertyUtils.getProperty(obj1, method);
				obj2 = PropertyUtils.getProperty(obj2, method);
				if (obj1 == null) {
					return direction * -1;
				} else if (obj2 == null) {
					return direction;
				} else if (obj1 instanceof Comparable) {
					return ((Comparable<Object>) obj1).compareTo(obj2) * direction;
				}
			} catch (Exception e) {
				// TODO: do not swallow this!
				e.printStackTrace();
			}
			return 0;
		}
	}

	/** Commons logger. */
	private static final Log	LOGGER	= LogFactory.getLog(DefaultValueListHandlerImpl.class);

	/** The configuration for this implementation. * */
	private Configuration		config	= new Configuration();

	/**
	 * Creates a new instance of ValueListHandler
	 */
	public DefaultValueListHandlerImpl() {
	}

	/**
	 * @param name of adapter
	 * @return ValueListAdapter
	 */
	private ValueListAdapter getAdapter(String name) {
		ValueListAdapter adapter = config.getAdapter(name);
		if (adapter == null) {
			throw new NullPointerException("Adapter could not be located: " + name);
		}
		return adapter;
	}

	/**
	 * @return Returns the config.
	 */
	public Configuration getConfig() {
		return config;
	}

	/**
	 * @see com.mlw.vlh.ValueListHander.getValueList(ValueListInfo info, String name)
	 */
	@SuppressWarnings({
				"rawtypes", "unchecked"})
	public <X> ValueList<X> getValueList(String name, ValueListInfo info) {
		if (info == null) {
			info = new ValueListInfo();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating a new ValueListInfo for the adapter '" + name + "'.");
			}
		}
		info.getFilters().put(ValueListInfo.VALUE_LIST_NAME, name);
		ValueListAdapter adapter = getAdapter(name);
		ValueList<X> valueList = adapter.getValueList(name, info);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("The ValueList was loaded from the adapter '" + name + "'.");
		}
		if ((adapter.getAdapterType() & ValueListAdapter.DO_FOCUS) == ValueListAdapter.DO_FOCUS) {
			throw new NullPointerException("Not yet implemented!");
		}
		if ((adapter.getAdapterType() & ValueListAdapter.DO_FILTER) == ValueListAdapter.DO_FILTER) {
			throw new NullPointerException("Not yet implemented!");
		}
		if ((adapter.getAdapterType() & ValueListAdapter.DO_SORT) == ValueListAdapter.DO_SORT) {
			if (valueList.getValueListInfo() != null
						&& valueList.getValueListInfo().getSortingColumn() != null
						&& valueList.getValueListInfo().getSortingDirection() != null
						&& valueList.getList() != null) {
				Collections.sort(valueList.getList(),
					new BeanComparator(valueList.getValueListInfo().getSortingColumn(), valueList.getValueListInfo()
						.getSortingDirection()
						.intValue()));
				LOGGER.debug("The ValueList was sorted by post process.");
			}
		}
		if ((adapter.getAdapterType() & ValueListAdapter.DO_PAGE) == ValueListAdapter.DO_PAGE) {
			if (valueList.getValueListInfo() != null && valueList.getList() != null) {
				int pagingPage = valueList.getValueListInfo().getPagingPage();
				int pagingNumberPer = valueList.getValueListInfo().getPagingNumberPer();
				int totalNumberOfEntries = valueList.getValueListInfo().getTotalNumberOfEntries();
				if (pagingNumberPer < totalNumberOfEntries) {
					int start = (pagingPage - 1) * pagingNumberPer;
					if (start >= totalNumberOfEntries) {
						start = ((totalNumberOfEntries - 1) / pagingNumberPer) * pagingNumberPer;
					}
					int end = Math.min(start + pagingNumberPer, valueList.getList().size());
					List<X> list = valueList.getList().subList(start, end);
					valueList = new DefaultListBackedValueList(list, valueList.getValueListInfo());
					LOGGER.debug("The ValueList was paged by post process.");
				}
			}
		}
		return valueList;
	}
}