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
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.4 $ $Date: 2005/11/23 14:37:14 $
 */
public class ExcelDisplayProvider implements DisplayProvider {

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getMimeType()
	 */
	public String getMimeType() {
		return "application/vnd.ms-excel";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getHeaderRowPreProcess()
	 */
	public String getHeaderRowPreProcess() {
		return "";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getHeaderCellPreProcess()
	 */
	public String getHeaderCellPreProcess(ColumnInfo columnInfo, ValueListInfo info) {
		return "";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getHeaderLabel(org.webframe.web.page.web.tag.ColumnInfo,
	 *      org.webframe.web.page.web.tag.TableInfo, org.webframe.web.page.ValueListInfo)
	 */
	public String getHeaderLabel(ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo info, Map<String, Object> includeParameters) {
		return columnInfo.getTitle();
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getHeaderCellPostProcess()
	 */
	public String getHeaderCellPostProcess() {
		return "\t";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getHeaderRowPostProcess()
	 */
	public String getHeaderRowPostProcess() {
		return "\n";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getRowPreProcess(net.mlw.util.web.tag.Attributes)
	 */
	public String getRowPreProcess(Attributes attributes) {
		return "";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getCellPreProcess(net.mlw.util.web.tag.Attributes)
	 */
	public String getCellPreProcess(Attributes attributes) {
		return "";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getCellPostProcess()
	 */
	public String getCellPostProcess() {
		return "\t";
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.HtmlDisplayProvider#getRowPostProcess()
	 */
	public String getRowPostProcess() {
		return "\n";
	}

	public boolean doesIncludeBodyContent() {
		return false;
	}

	/**
	 * @see org.webframe.web.page.web.tag.support.DisplayProvider#emphase(java.lang.String,
	 *      java.lang.String)
	 */
	public String emphase(String text, String emphasisPattern, String style) {
		// AAA add emphase escape charecterc for excel display provider
		return text;
	}

	public String getNestedHeaderPreProcess(ColumnInfo columnInfo, ValueListInfo info) {
		return "";
	}

	public String getNestedHeaderPostProcess() {
		return "";
	}
}