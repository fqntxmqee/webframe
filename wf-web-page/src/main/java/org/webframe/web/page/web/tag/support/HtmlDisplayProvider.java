/**
 * Copyright (c) 2003 held jointly by the individual authors.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *  > http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.web.tag.support;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListConfigBean;
import org.webframe.web.page.web.tag.TableInfo;

/**
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.20 $ $Date: 2005/11/23 14:37:15 $
 */
public class HtmlDisplayProvider extends AbstractHTMLDisplayProvider {

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER					= LogFactory.getLog(HtmlDisplayProvider.class);

	private String					imageHome				= "images";

	private boolean				preAppendContextPath	= false;

	private boolean				usePadding				= true;

	private boolean				useNoWrap				= true;

	/**
	 * @see #setImageHome(String)
	 * @return base image home.
	 */
	public String getImageHome() {
		return imageHome;
	}

	/**
	 * @see #setPreAppendContextPath(boolean)
	 * @return true, false
	 */
	public boolean isPreAppendContextPath() {
		return preAppendContextPath;
	}

	/**
	 * @return
	 */
	public boolean isUseNoWrap() {
		return useNoWrap;
	}

	/**
	 * @return
	 */
	public boolean isUsePadding() {
		return usePadding;
	}

	/**
	 * Pre append to <code>imageHome</code> property application context dir. Default is set to false
	 * due to backward compatibility. <h4>Example <h4>Considere that valuelist is the name of
	 * application context. <ul> <li>true</li> <b>valuelist/ </b>images <li>false</li> images </ul>
	 * 
	 * @param preAppendContextPath
	 */
	public void setPreAppendContextPath(boolean preAppendAppDir) {
		this.preAppendContextPath = preAppendAppDir;
	}

	/**
	 * Set the default path for images used in paging, focusing and so on. Default is set to
	 * <code>images</code>.
	 * 
	 * @see #setPreAppendContextPath(boolean)
	 * @param dir <h4>Example <h4> <ul> <li>/myProject/images/microsoftLook</li> generate html for
	 *           image like this &lt; alt="Sort" src="/myProject/images/microsoftLook/..." &gt;
	 *           <li>images/microsoftLook</li> generate html for image like this &lt; alt="Sort"
	 *           src="images/microsoftLook/..." &gt; </ul>
	 */
	public void setImageHome(String dir) {
		imageHome = dir;
	}

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	@Override
	public String getHeaderCellPreProcess(ColumnInfo columnInfo, ValueListInfo info) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n    <th");
		if (useNoWrap) {
			sb.append(" nowrap=\"true\"");
		}
		if (columnInfo != null) {
			if (columnInfo.getToolTip() != null) {
				sb.append(" title=\"").append(columnInfo.getToolTip()).append("\""); // html attribute
																											// title renderes a
																											// toolTip
			}
			if (columnInfo.getAttributes() != null) {
				sb.append(" ").append(columnInfo.getAttributes());
			}
		}
		sb.append(">");
		if (usePadding) {
			sb.append("&nbsp;");
		}
		return sb.toString();
	}

	/**
	 * Formats the text to be displayed as the header by wraping it in a link if sorting is enabled.
	 * Alt (hint) is localized, please define in your property file for messages the property
	 * "sorting"
	 * 
	 * @param columnInfo The ColumnInfo.
	 * @param tableInfo The TableInfo.
	 * @param info The ValueListInfo.
	 * @return The formated HTML.
	 */
	@Override
	public String getHeaderLabel(ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo info, Map<String, Object> includeParameters) {
		ValueListConfigBean config = tableInfo.getConfig();
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
			if (columnInfo.getAdapterPropertyName().equals(column)) {
				if (usePadding) {
					sb.append("&nbsp;");
				}
				sb.append("<img src=\"")
					.append(getImageHome((HttpServletRequest) tableInfo.getPageContext().getRequest()))
					.append("/sort(");
				sb.append((ValueListInfo.ASCENDING.equals(direction) ? ValueListInfo.DESCENDING : ValueListInfo.ASCENDING));
				sb.append(").png\" border=\"0\"/>");
			} else if (columnInfo.getDefaultSort() != null) {
				Locale locale = config.getLocale(
					(HttpServletRequest) (tableInfo.getPageContext().getRequest()));
				String altSort;
				try {
					altSort = config.getDisplayHelper().help(tableInfo.getPageContext(),
						config.getMessageSource().getMessage("sorting", null, "Sort", locale));
				} catch (JspException e) {
					LOGGER.error("getHeaderLabel() - Error getting property 'sorting' : "
								+ e.getMessage()
								+ " Locale locale = "
								+ locale
								+ ", String column = "
								+ column
								+ " using defalt hint for sorting images.");
					altSort = "Sort";
				}
				sb.append(((usePadding) ? "&nbsp;" : ""))
					.append("<img alt=\"")
					.append(altSort)
					.append("\" src=\"")
					.append(getImageHome((HttpServletRequest) tableInfo.getPageContext().getRequest()))
					.append("/sort(null).png\" border=\"0\"/>");
			}
			return sb.toString();
		} else {
			return columnInfo.getTitle();
		}
	}

	/**
	 * Renders a link as a header of the column if the sorting is enabled. Subclasses can overide or
	 * extend the method to provide different behaviour.
	 * 
	 * @param sb StringBuffer to render into
	 * @param columnInfo The ColumnInfo.
	 * @param tableInfo The TableInfo.
	 * @param info The ValueListInfo.
	 * @param parameters Map of parameters
	 */
	protected void renderHeaderLabelLink(StringBuffer sb, ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo info, Map<String, Object> parameters) {
		// String column = info.getSortingColumn();
		// Integer direction = info.getSortingDirection();
		sb.append("<a href=\"").append(tableInfo.getUrl());
		sb.append(tableInfo.getConfig().getLinkEncoder().encode(tableInfo.getPageContext(), parameters));
		sb.append("\">").append(columnInfo.getTitle()).append("</a>");
	}

	/**
	 * Return imageHome, if is needed, return it with prefix "appDir"
	 * 
	 * @param pageContext
	 * @param HttpServletRequest -if null, skip appending appDir-contextPath
	 * @return String that represent images home dir.
	 */
	public String getImageHome(HttpServletRequest request) {
		if (preAppendContextPath && request != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Preappending context path='"
							+ request.getContextPath()
							+ "' to image Home '"
							+ imageHome
							+ "'.");
			}
			return request.getContextPath() + "/" + imageHome;
		}
		return imageHome;
	}

	/**
	 * Get the HTML that comes after the column text.
	 * 
	 * @return The HTML that comes after the column text.
	 */
	@Override
	public String getHeaderCellPostProcess() {
		return ((usePadding) ? "&nbsp;" : "") + "</th>";
	}

	@Override
	public String getCellPreProcess(Attributes attributes) {
		return (attributes == null) ? "\n<td>" : "\n<td "
					+ attributes.getCellAttributesAsString()
					+ ">"
					+ ((usePadding) ? "&nbsp;" : "");
	}

	@Override
	public String getCellPostProcess() {
		return ((usePadding) ? "&nbsp;" : "") + "</td>";
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Images home(without context path): " + getImageHome(null) + " - " + super.toString();
	}

	/**
	 * @param usePadding The usePadding to set.
	 */
	public void setUsePadding(boolean usePadding) {
		this.usePadding = usePadding;
	}

	/**
	 * @param useNoWrap The useNoWrap to set.
	 */
	public void setUseNoWrap(boolean useNoWrap) {
		this.useNoWrap = useNoWrap;
	}
}