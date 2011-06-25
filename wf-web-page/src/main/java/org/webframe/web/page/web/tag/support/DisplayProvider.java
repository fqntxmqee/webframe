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

import java.util.Map;

import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.tag.TableInfo;

/**
 * This interface defines how the valuelist should be rendered.
 * 
 * @author mwilson TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public interface DisplayProvider {

	boolean doesIncludeBodyContent();

	String getMimeType();

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	String getHeaderRowPreProcess();

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	String getHeaderCellPreProcess(ColumnInfo columnInfo, ValueListInfo info);

	/**
	 * Formats the text to be displayed as the header by waraping it in a link if sorting is enabled.
	 * 
	 * @param columnInfo The ColumnInfo.
	 * @param tableInfo The TableInfo.
	 * @param info The ValueListInfo.
	 * @return The formated HTML.
	 */
	String getHeaderLabel(ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo info, Map<String, Object> includeParameters);

	/**
	 * Get the HTML that comes after the column text.
	 * 
	 * @return The HTML that comes after the column text.
	 */
	String getHeaderCellPostProcess();

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	String getHeaderRowPostProcess();

	String getRowPreProcess(Attributes attributes);

	String getCellPreProcess(Attributes attributes);

	String getCellPostProcess();

	String getRowPostProcess();

	/**
	 * Get the HTML that emphase text.
	 * 
	 * @param style CSS className for the span tag.
	 * @param text a String that represent source text to search the emphasis pattern.
	 * @param emphasisPattern a String that will be emphased in text.
	 * @return String Text with emphasised pattern.
	 * @author Andrej Zachar
	 */
	String emphase(String text, String emphasisPattern, String style);

	String getNestedHeaderPreProcess(ColumnInfo columnInfo, ValueListInfo info);

	String getNestedHeaderPostProcess();
}