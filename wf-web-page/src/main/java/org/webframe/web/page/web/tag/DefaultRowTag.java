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

package org.webframe.web.page.web.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListConfigBean;
import org.webframe.web.page.web.tag.support.ColumnInfo;
import org.webframe.web.page.web.tag.support.DisplayProvider;
import org.webframe.web.page.web.tag.support.Spacer;
import org.webframe.web.page.web.tag.support.ValueListNullSpacer;
import org.webframe.web.page.web.util.JspUtils;

/**
 * This tag creates a table. It is ment to be used along with vlh:root tag. If the valuelist is
 * empty, render only header.
 * 
 * @todo Document this tag.
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.37 $ $Date: 2005/12/15 12:49:12 $
 */
public class DefaultRowTag extends ConfigurableTag {

	private static final long	serialVersionUID	= 4050760477275928119L;

	/** Commons logger. */
	private static final Log	LOGGER				= LogFactory.getLog(DefaultRowTag.class);

	/** Parent root tag. */
	private ValueListSpaceTag	rootTag;

	private DisplayProvider		displayProvider;

	private List<ColumnInfo>	columns				= new ArrayList<ColumnInfo>();

	protected int					currentRowNumber	= 0;

	protected Object				beanInPageScope;

	/** The name and type of the bean put in the pageContext. */
	protected String				bean;

	/**
	 * True - Disable rendering of the header. False - Check if at least one column has a title, if
	 * yes, render header row, if not, skip it.
	 */
	private boolean				disableHeader		= false;

	/**
	 * How to render the header in case of nested value list (the default is <code>true</code>).
	 * <br><code>false</code> - the header of the nested value list is rendered as a part of the top
	 * value list <br><code>true</code> - the header is rendered for each nested valuelist
	 */
	private boolean				nestedHeader		= true;

	/**
	 * Initialization is called at the beginning of <code>doStart</code>. Subclasses have to call
	 * either <code>super.init()</code> or <code>super.doStart()</code>.
	 * 
	 * @throws JspException
	 */
	protected void init() throws JspException {
		this.columns.clear();
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		init();
		// If the valuelist is empty, render only header
		ValueList valueList = getRootTag().getValueList();
		if (valueList == null || valueList.getList() == null || valueList.getList().size() == 0) {
			getRootTag().setValueList(ValueListNullSpacer.getInstance());
			LOGGER.warn("ValueList '"
						+ getRootTag().getTableInfo().getName()
						+ "' is empty or null. Continue working with the singleton ValueListNullSpacer.");
		}
		if (getDisplayProvider().getMimeType() != null) {
			try {
				pageContext.getResponse().setContentType(getDisplayProvider().getMimeType());
			} catch (Exception e) {
				LOGGER.error("DefaultRowTag.doStartTag() exception...", e);
				throw new JspException(e);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Starting procesing rows of the table '"
						+ getRootTag().getTableInfo().getId()
						+ "' with the ValueList '"
						+ getRootTag().getTableInfo().getName()
						+ "'.");
		}
		processIteration();
		return EVAL_BODY_AGAIN;
	}

	/**
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 * @todo figure out why release is not working.
	 */
	public int doAfterBody() throws JspException {
		// If this is the first row, then print the column headers!
		if (currentRowNumber == 0 && getRootTag() != null) {
			if (isEnabledHeader()) {
				DefaultColumnTag parentColumnTag = (DefaultColumnTag) findAncestorWithClass(this, DefaultColumnTag.class);
				if (parentColumnTag == null || isNestedHeader()) {
					StringBuffer sb = new StringBuffer();
					renderHeaderRow(sb, getColumns(), getRootTag().getTableInfo(), getRootTag().getValueList()
						.getValueListInfo(), getRootTag().getTableInfo().getParameters());
					JspUtils.writePrevious(pageContext, sb.toString());
					columns.clear();
				} else {
					// nested valuelist
					parentColumnTag.setNestedColumnInfoList(columns);
					columns = new ArrayList<ColumnInfo>();
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Header of the table '" + getRootTag().getTableInfo().getId() + "' is skiped.");
				}
				columns.clear();
			}
		}
		if (beanInPageScope == null || beanInPageScope instanceof Spacer) {
			// release();
			return SKIP_BODY;
		}
		// Render the output from this iteration to the output stream
		if (bodyContent != null) {
			String style = getRowStyleClass();
			pageContext.setAttribute(bean + "Style", style);
			appendClassCellAttribute(style);
			JspUtils.writePrevious(pageContext, getDisplayProvider().getRowPreProcess(getCellAttributes()));
			if (getDisplayProvider().doesIncludeBodyContent()) {
				JspUtils.writePrevious(pageContext, bodyContent.getString());
			} else {
				String html = bodyContent.getString().replaceAll("\\n", "").replaceAll("\\r", "");
				JspUtils.writePrevious(pageContext, html);
			}
			JspUtils.writePrevious(pageContext, getDisplayProvider().getRowPostProcess());
			bodyContent.clearBody();
		}
		currentRowNumber++;
		return processIteration();
	}

	/**
	 * Renders header row. Subclasses can overide or extend the method to provide different
	 * behaviour.
	 * 
	 * @param sb
	 * @param tableInfo
	 * @param vlInfo
	 * @param parameters
	 * @throws JspException
	 */
	protected void renderHeaderRow(StringBuffer sb, List<ColumnInfo> columns, TableInfo tableInfo, ValueListInfo vlInfo, Map<String, Object> parameters)
				throws JspException {
		sb.append(getDisplayProvider().getHeaderRowPreProcess());
		for (Iterator<ColumnInfo> iter = columns.iterator(); iter.hasNext();) {
			renderHeaderCell(sb, iter.next(), tableInfo, vlInfo, parameters);
		}
		sb.append(getDisplayProvider().getHeaderRowPostProcess());
	}

	/**
	 * Renders header cell. Subclasses can overide or extend the method to provide different
	 * behaviour.
	 * 
	 * @param sb
	 * @param columnInfo
	 * @param tableInfo
	 * @param vlInfo
	 * @param parameters
	 * @throws JspException
	 */
	protected void renderHeaderCell(StringBuffer sb, ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo vlInfo, Map<String, Object> parameters)
				throws JspException {
		sb.append(getDisplayProvider().getHeaderCellPreProcess(columnInfo, vlInfo));
		if (columnInfo.getNestedList() != null) {
			renderNestedHeader(sb, columnInfo, tableInfo, vlInfo, parameters);
		} else {
			sb.append(getDisplayProvider().getHeaderLabel(columnInfo, tableInfo, vlInfo, parameters));
		}
		sb.append(getDisplayProvider().getHeaderCellPostProcess());
	}

	protected void renderNestedHeader(StringBuffer sb, ColumnInfo columnInfo, TableInfo tableInfo, ValueListInfo vlInfo, Map<String, Object> parameters)
				throws JspException {
		sb.append(getDisplayProvider().getNestedHeaderPreProcess(columnInfo, null));
		renderHeaderRow(sb, columnInfo.getNestedList(), tableInfo, vlInfo, parameters);
		sb.append(getDisplayProvider().getNestedHeaderPostProcess());
	}

	/**
	 * This option could be overwritten by disableHeader (default set to false)
	 * 
	 * @return true when at least one column's title is not null. false all titles are null.
	 */
	protected boolean isEnabledHeader() {
		if (disableHeader) {
			return false;
		}
		return hasTitle(this.columns);
	}

	private boolean hasTitle(List<ColumnInfo> columns) {
		for (Iterator<ColumnInfo> iter = columns.iterator(); iter.hasNext();) {
			ColumnInfo columnInfo = iter.next();
			if (columnInfo.getNestedList() != null
						&& hasTitle(columnInfo.getNestedList())
						|| columnInfo.getTitle() != null) {
				return true;
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("No titles are specified in the table");
		}
		return false;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doEndTag() throws JspException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("End of processing rows of the table '"
						+ getRootTag().getTableInfo().getId()
						+ "' with the ValueList '"
						+ getRootTag().getTableInfo().getName()
						+ "'.");
		}
		release();
		return EVAL_PAGE;
	}

	/**
	 * Produces one row.
	 * 
	 * @return
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 * @throws JspException is an error occurs.
	 */
	protected int processIteration() throws JspException {
		if (getRootTag() != null && getRootTag().getValueList() != null && getRootTag().getValueList().hasNext()) {
			pageContext.setAttribute(bean + "RowNumber", new Integer(currentRowNumber));
			beanInPageScope = getRootTag().getValueList().next();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("In the row no.'"
							+ currentRowNumber
							+ "' was setted the JavaBean '"
							+ bean
							+ "' in the pageContext.");
			}
			if (beanInPageScope == null || beanInPageScope instanceof Spacer) {
				pageContext.removeAttribute(bean);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("In the row no.'"
								+ currentRowNumber
								+ "' was not found any ValueList's List's (JavaBean) items to render.");
				}
			} else {
				pageContext.setAttribute(bean, beanInPageScope);
			}
			return (EVAL_BODY_AGAIN);
		} else {
			// @todo check why you like to call release - it sets BodyTagSupport.parent to null!
			// release();
			return SKIP_BODY;
		}
	}

	public final ValueListSpaceTag getRootTag() throws JspException {
		if (rootTag == null) {
			rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		}
		return rootTag;
	}

	/**
	 * @return Returns the displayProvider.
	 * @throws JspException
	 */
	public DisplayProvider getDisplayProvider() throws JspException {
		if (displayProvider == null) {
			displayProvider = getRootTag().getConfig().getDisplayProvider("html");
			if (displayProvider == null) {
				LOGGER.error("Error getting default DisplayProvider (html).");
				displayProvider = ValueListConfigBean.DEFAULT_DISPLAY_PROVIDER;
			}
		}
		return displayProvider;
	}

	public void setDisplay(String display) throws JspException {
		this.displayProvider = getRootTag().getConfig().getDisplayProvider(display);
	}

	/**
	 * Gets the current row in the iteration.
	 * 
	 * @return The current row in the iteration.
	 */
	public int getCurrentRowNumber() {
		return currentRowNumber;
	}

	/**
	 * Adds a column to this table.
	 * 
	 * @param column The Column to add.
	 */
	public void addColumnInfo(ColumnInfo column) {
		columns.add(column);
	}

	/**
	 * Returns list of <code>ColumnInfo</code> objects.
	 * 
	 * @return Returns the columns.
	 */
	protected List<ColumnInfo> getColumns() {
		return columns;
	}

	/**
	 * Sets the name of the bean put in the pageContext.
	 * 
	 * @param bean The name of the bean put in the pageContext.
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * Gets the name of the bean put in the pageContext.
	 * 
	 * @return The name of the bean put in the pageContext.
	 */
	public String getBeanName() {
		return bean;
	}

	/**
	 * @param focusRowNumber (0 is considered to be first row) The focusRowNumber to set.
	 */
	public void setFocusRowNumber(String focusRowNumber) {
		try {
			Integer.parseInt(focusRowNumber);
		} catch (NumberFormatException e) {
		}
	}

	/**
	 * @param disableHeader The disableHeader to set.
	 */
	public void setDisableHeader(boolean disableHeader) {
		this.disableHeader = disableHeader;
	}

	/**
	 * @return Returns the nestedHeader.
	 * @see #setNestedHeader(boolean)
	 */
	public boolean isNestedHeader() {
		return nestedHeader;
	}

	/**
	 * Set how to render the header in case of nested value list (the default is <code>true</code>).
	 * 
	 * @param nestedHeader <code>false</code> - the header of the nested value list is rendered as a
	 *           part of the top value list <code>true</code> - the header is rendered for each
	 *           nested valuelist
	 */
	public void setNestedHeader(boolean nestedHeader) {
		this.nestedHeader = nestedHeader;
	}

	/**
	 * @return style
	 * @throws JspException
	 */
	public String getRowStyleClass() throws JspException {
		ValueListConfigBean config = getRootTag().getConfig();
		if (currentRowNumber == getRootTag().getValueList().getValueListInfo().getFocusedRowNumberInTable()) {
			return config.getFocusedRowStyle();
		} else {
			return config.getStylePrefix() + (currentRowNumber % config.getStyleCount());
		}
	}

	private void reset() {
		this.rootTag = null;
		this.bean = null;
		this.beanInPageScope = null;
		this.columns.clear();
		this.currentRowNumber = 0;
		this.disableHeader = false;
		this.nestedHeader = true;
		this.displayProvider = null;
		this.currentRowNumber = 0;
	}

	/**
	 * Called on a Tag handler to release state. The page compiler guarantees that JSP page
	 * implementation objects will invoke this method on all tag handlers, but there may be multiple
	 * invocations on doStartTag and doEndTag in between.
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		reset();
	}
}