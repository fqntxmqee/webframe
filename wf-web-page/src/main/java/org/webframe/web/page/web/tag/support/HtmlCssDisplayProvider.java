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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the  
 * GNU Lesser General Public License for more details.  
 *  
 * You should have received a copy of the GNU Lesser General Public License  
 * along with this library; if not, write to the Free Software Foundation,  
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.  
 *  
 * > http://www.gnu.org/copyleft/lesser.html  
 * > http://www.opensource.org/licenses/lgpl-license.php 
 */

package org.webframe.web.page.web.tag.support;

import java.util.HashMap;
import java.util.Map;

import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.tag.TableInfo;

/**
 * @author Sebastian Beigel, Andrej Zachar
 * @version $Revision: 1.8 $ $Date: 2005/11/23 14:37:14 $
 */
public class HtmlCssDisplayProvider extends AbstractHTMLDisplayProvider {

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	public String getHeaderCellPreProcess(ColumnInfo columnInfo, ValueListInfo info) {
		StringBuffer sb = new StringBuffer(255); // reasonable initial size to avoid the need to
																// prolonge it
		sb.append("\n<th ");
		if (columnInfo != null) {
			String attributes = columnInfo.getAttributes();
			if (attributes != null) {
				sb.append(attributes);
			}
			if (columnInfo.getDefaultSort() != null) {
				String column = info.getSortingColumn();
				Integer direction = info.getSortingDirection();
				if (columnInfo.getAdapterPropertyName().equals(column)) {
					sb.append(" class=\"sortable ")
						.append(((ValueListInfo.ASCENDING.equals(direction) ? "orderAsc" : "orderDesc")))
						.append("\"");
				} else {
					sb.append(" class=\"sortable\"");
				}
			} // columnInfo.getDefaultSort() != null
			if (columnInfo.getToolTip() != null) {
				sb.append(" title=\"").append(columnInfo.getToolTip()).append("\""); // html attribute
																											// title renderes a
																											// toolTip
			} // columnInfo.getToolTip() != null
		}
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Formats the text to be displayed as the header by waraping it in a link if sorting is enabled.
	 * 
	 * @param columnInfo The ColumnInfo.
	 * @param tableInfo The TableInfo.
	 * @param info The ValueListInfo.
	 * @return The formated HTML.
	 */
	public String getHeaderLabel(ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo info, Map<String, Object> includeParameters) {
		Map<String, Object> parameters = new HashMap<String, Object>(includeParameters);
		if (columnInfo.getDefaultSort() != null) {
			// Get the current sort column and direction.
			String column = info.getSortingColumn();
			Integer direction = info.getSortingDirection();
			parameters.put(ValueListInfo.PAGING_NUMBER_PER + tableInfo.getId(), String.valueOf(info.getPagingNumberPer()));
			parameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(), "1");
			parameters.put(ValueListInfo.SORT_COLUMN + tableInfo.getId(), columnInfo.getAdapterPropertyName());
			parameters.put(ValueListInfo.SORT_DIRECTION + tableInfo.getId(),
				((columnInfo.getAdapterPropertyName().equals(column)) ? (ValueListInfo.ASCENDING.equals(direction)
							? ValueListInfo.DESCENDING
							: ValueListInfo.ASCENDING) : columnInfo.getDefaultSort()));
			if (info.isFocusEnabled()) {
				parameters.put(ValueListInfo.DO_FOCUS + tableInfo.getId(), info.isDoFocusAgain() ? "true" : "false");
				if (info.getFocusProperty() != null) {
					parameters.put(ValueListInfo.FOCUS_PROPERTY + tableInfo.getId(), info.getFocusProperty());
				}
				if (info.getFocusValue() != null) {
					parameters.put(ValueListInfo.FOCUS_VALUE + tableInfo.getId(), info.getFocusValue());
				}
			}
			StringBuffer sb = new StringBuffer();
			renderHeaderLabelLink(sb, columnInfo, tableInfo, info, parameters);
			return sb.toString();
		} else {
			return columnInfo.getTitle();
		}
	}

	/**
	 * Renders a link as a header of the column if the sorting is enabled. Defined as protected to
	 * allow rewriting of an inhrited class.
	 * 
	 * @param sb StringBuffer to render into
	 * @param columnInfo The ColumnInfo.
	 * @param tableInfo The TableInfo.
	 * @param info The ValueListInfo.
	 * @param parameters Map of parameters
	 */
	protected void renderHeaderLabelLink(StringBuffer sb, ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo info, Map<String, Object> parameters) {
		sb.append("<a href=\"").append(tableInfo.getUrl());
		sb.append(tableInfo.getConfig().getLinkEncoder().encode(tableInfo.getPageContext(), parameters));
		sb.append("\">").append(columnInfo.getTitle()).append("</a>");
	}

	/**
	 * Get the HTML that comes after the column text.
	 * 
	 * @return The HTML that comes after the column text.
	 */
	public String getHeaderCellPostProcess() {
		return "</th>";
	}

	public String getCellPreProcess(Attributes attributes) {
		return (attributes == null) ? "\n<td>" : "\n<td " + attributes.getCellAttributesAsString() + ">";
	}

	public String getCellPostProcess() {
		return "</td>";
	}
}
