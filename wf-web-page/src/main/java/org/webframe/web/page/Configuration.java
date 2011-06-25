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

import java.util.Map;

/**
 * Holds the configuration for the value list handler.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.4 $ $Date: 2005/11/23 14:18:53 $
 */
public class Configuration {

	/** Commons logger. */
	// private static final Log LOGGER = LogFactory.getLog(Configuration.class);
	/** Holds all the adapters. The name of the adapter is the key. */
	private Map<String, ValueListAdapter>	adapters			= null;

	/** The default adapter to pass back if none are found. **/
	private ValueListAdapter					defaultAdapter	= null;

	/**
	 * Gets the adapter with the given name. Returns the defaultAdapter if the given name could not
	 * be found.
	 * 
	 * @param name The name of the adapter.
	 * @return The adapter with the given name, or the default.
	 */
	public ValueListAdapter getAdapter(String name) {
		ValueListAdapter adapter = (ValueListAdapter) adapters.get(name);
		if (adapter == null) {
			adapter = getDefaultAdapter();
		}
		if (adapter == null) {
			throw new NullPointerException("Adapter named: " + name + ", not found, and no default was declared.");
		}
		return adapter;
	}

	/**
	 * Sets the Map of adapters.
	 * 
	 * @param adapters The adapters to set.
	 */
	public void setAdapters(Map<String, ValueListAdapter> adapters) {
		this.adapters = adapters;
	}

	/**
	 * Gets the default adapter to pass back if none are found.
	 * 
	 * @return Returns the defaultAdapter.
	 */
	public ValueListAdapter getDefaultAdapter() {
		return defaultAdapter;
	}

	/**
	 * Sets the default adapter to pass back if none are found.
	 * 
	 * @param defaultAdapter The defaultAdapter to set.
	 */
	public void setDefaultAdapter(ValueListAdapter defaultAdapter) {
		this.defaultAdapter = defaultAdapter;
	}
}