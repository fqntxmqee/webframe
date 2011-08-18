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

package org.webframe.web.page.adapter;

import org.webframe.web.page.DefaultListBackedValueList;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListAdapter;
import org.webframe.web.page.ValueListInfo;

/**
 * This decorator is a lazy way to implement caching. This is ideal for select boxes and the like.
 * <p> Note that to make this thread safe sorting and paging must be defined in the parent
 * ValueListAdapter </p>
 * 
 * @author mwilson
 */
public class ValueListAdapterCacheDecorator implements ValueListAdapter {

	/** The duration in milliseconds to allow cached data to stick around. **/
	private long					cacheTimeout		= Long.MAX_VALUE;

	/** The time in milliseconds the cache was created. **/
	private long					cacheCreateTime	= -1;

	/** The time in milliseconds the cache should be refreshed. **/
	private long					nextRefresh			= System.currentTimeMillis();

	/** The source for the data. **/
	private ValueListAdapter	decoratedValueListAdapter;

	/** The cached valuelist. **/
	private ValueList<?>			valueList;

	/**
	 * @see org.webframe.web.page.ValueListAdapter#getAdapterType()
	 */
	public int getAdapterType() {
		return decoratedValueListAdapter.getAdapterType();
	}

	/**
	 * @see org.webframe.web.page.ValueListAdapter#getValueList(java.lang.String,
	 *      org.webframe.web.page.ValueListInfo)
	 */
	@SuppressWarnings({
				"rawtypes", "unchecked"})
	public <X> ValueList<X> getValueList(String name, ValueListInfo info) {
		// Check to see if we ever invalidate the cache.
		if (valueList == null || cacheTimeout != Long.MAX_VALUE) {
			if (nextRefresh < System.currentTimeMillis()) {
				cacheCreateTime = System.currentTimeMillis();
				nextRefresh = cacheCreateTime + cacheTimeout;
				valueList = decoratedValueListAdapter.getValueList(name, info);
			}
		}
		return new DefaultListBackedValueList(valueList.getList(), valueList.getValueListInfo());
	}

	/**
	 * Sets the duration in milliseconds to allow cached data to stick around.
	 * 
	 * @param cachTimeout Duration in milliseconds.
	 */
	public void setCacheTimeout(long cacheTimeout) {
		this.cacheTimeout = cacheTimeout;
	}

	/**
	 * The underling ValueListAdapter that retrieves the ValueList to be cached.
	 * 
	 * @param decoratedValueListAdapter The parent ValueListAdapter.
	 */
	public void setParent(ValueListAdapter decoratedValueListAdapter) {
		this.decoratedValueListAdapter = decoratedValueListAdapter;
	}
}