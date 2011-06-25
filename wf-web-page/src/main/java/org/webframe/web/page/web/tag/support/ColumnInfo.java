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

package org.webframe.web.page.web.tag.support;

import java.util.List;

/**
 * Holds attributes of a column (title, adapterProperty etc). It's used to transfer information
 * about the column among the various classes that work with it.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.4 $ $Date: 2005/11/23 14:36:19 $
 */
public class ColumnInfo {

	/** The title of the column. */
	private String					title;

	/** The tooltip of the column's title (shown when a mouse pointer is over the title). */
	private String					toolTip	= null;

	/** The header of a nested value list. */
	private List<ColumnInfo>	nestedList;

	/** The adapter name of the method that returns the value to display. */
	private String					adapterPropertyName;

	/** The default sort direction. */
	private Integer				defaultSort;

	private String					attributes;

	/**
	 * Default no argument bean constructor.
	 */
	public ColumnInfo() {
	}

	/**
	 * Creates a column info with toolTip set to the given value.
	 * 
	 * @param title The title of the column.
	 * @param toolTip The tool tip of the column (shown when a mouse pointer is over the title).
	 * @param adapterPropertyName The adapter property name of the method that returns the value to
	 *           display.
	 * @param defaultSort The default sort direction.
	 * @param attributes Other attributes of the resulting tag (e.g.
	 *           "abbr='an abbreviation' id='myTHId'").
	 * @see #ColumnInfo(String, String, Integer, String)
	 */
	public ColumnInfo(String title, String toolTip, String propertyName, Integer defaultSort, String attributes) {
		this(title, propertyName, defaultSort, attributes);
		setToolTip(toolTip);
	}

	/**
	 * Default constructor.
	 * 
	 * @param title The title of the column.
	 * @param adapterPropertyName The adapter property name of the method that returns the value to
	 *           display.
	 * @param defaultSort The default sort direction.
	 */
	public ColumnInfo(String title, String propertyName, Integer defaultSort, String attributes) {
		this.title = title;
		this.adapterPropertyName = propertyName;
		this.defaultSort = defaultSort;
		this.attributes = attributes;
	}

	/**
	 * Gets the title of the column.
	 * 
	 * @return The title of the column.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the tooltip of the column's title (shown when a mouse pointer is over the title).
	 * 
	 * @return the tooltip of the column's title; returns null if the tooltip wasn't set.
	 * @see org.webframe.web.page.web.tag.DefaultColumnTag#getToolTip()
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * @return Returns the nestedList.
	 */
	public List<ColumnInfo> getNestedList() {
		return nestedList;
	}

	/**
	 * @param nestedList The nestedList to set.
	 */
	public void setNestedList(List<ColumnInfo> nestedList) {
		this.nestedList = nestedList;
	}

	/**
	 * Set the tooltip of the column's title (shown when a mouse pointer is over the title).
	 * 
	 * @param toolTip The tooltip of the column's title.
	 * @see org.webframe.web.page.web.tag.DefaultColumnTag#getToolTip()
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * Gets the adapter property name of the method that returns the value to display.
	 * 
	 * @return The adapter property name of the method that returns the value to display.
	 */
	public String getAdapterPropertyName() {
		return adapterPropertyName;
	}

	/**
	 * @param adapterPropertyName The adapterPropertyName to set.
	 */
	public void setAdapterPropertyName(String adapterPropertyName) {
		this.adapterPropertyName = adapterPropertyName;
	}

	/**
	 * Gets the default sort direction.
	 * 
	 * @return The default sort direction.
	 */
	public Integer getDefaultSort() {
		return defaultSort;
	}

	/**
	 * @param defaultSort The defaultSort to set.
	 */
	public void setDefaultSort(Integer defaultSort) {
		this.defaultSort = defaultSort;
	}

	/**
	 * @return Returns the attributes.
	 */
	public String getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes The attributes to set.
	 */
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
}