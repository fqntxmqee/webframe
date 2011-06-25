/**
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

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListConfigBean;
import org.webframe.web.page.web.tag.support.ColumnInfo;
import org.webframe.web.page.web.tag.support.DisplayProvider;
import org.webframe.web.page.web.util.JspUtils;

/**
 * This tag creates a column in a row, emphase wanted data and format column with specific locale.
 * If is it not specified, use spring LocaleResolver to obtain default locale.
 * 
 * @todo Document this tag.
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.23 $ $Date: 2006/01/06 10:53:54 $
 */
// This class was refactored with using BaseColumnTag. Some methods and fields were pull up.
public class DefaultColumnTag extends BaseColumnTag {

	private static final long	serialVersionUID		= -1160414311192942253L;

	/**
	 * Logger for this class
	 */
	private static final Log	LOGGER					= LogFactory.getLog(DefaultColumnTag.class);

	/** Supports column Grouping. **/
	private String					groupKey;

	private String					sum;

	/** Holder of default sort direction, null is not sortable. */
	private Integer				defaultSort;

	/** The javabean property name of this column used to get data from list returned by adapter. */
	private String					property;

	/** The property name to use to retrieve do for this columns in an adapter. */
	private String					adapterProperty		= null;

	private String					format;

	private String					defaultValue;

	/** The pattern to be emphasis displayed in the ordinary column cell. */
	private String					emphasisPattern		= null;

	/** Locale used in column */
	private Locale					locale					= null;

	private List<ColumnInfo>	nestedColumnInfoList	= null;

	/**
	 * @return Returns the adapterProperty. if it is null, return ordinary property.
	 */
	public String getAdapterProperty() {
		return adapterProperty == null ? property : adapterProperty;
	}

	/**
	 * @param sqlProperty The sql property name to use to retrieve do for this columns in adapter.
	 */
	public void setAdapterProperty(String sqlProperty) {
		this.adapterProperty = sqlProperty;
	}

	/**
	 * Initialization is called at the beginning of <code>doStart</code>. Subclasses have to call
	 * either <code>super.init()</code> or <code>super.doStart()</code>.
	 * 
	 * @throws JspException
	 */
	protected void init() throws JspException {
		if (bodyContent != null) {
			bodyContent.clearBody();
		}
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		init();
		return super.doStartTag();
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		DefaultRowTag rowTag = (DefaultRowTag) JspUtils.getParent(this, DefaultRowTag.class);
		ValueListConfigBean config = rootTag.getConfig();
		appendClassCellAttribute(rowTag.getRowStyleClass());
		if (locale == null) {
			locale = config.getLocaleResolver().resolveLocale((HttpServletRequest) pageContext.getRequest());
		}
		if (rowTag.getCurrentRowNumber() == 0) // if this is the 1st row == the one with cell headers
		{
			String titleKey = getTitleKey();
			String label = (titleKey == null) ? getTitle() : config.getMessageSource().getMessage(titleKey, null,
				titleKey, locale);
			ColumnInfo columnInfo = new ColumnInfo(config.getDisplayHelper().help(pageContext, label), getAdapterProperty(), defaultSort, getAttributes());
			// Process toolTip if any
			// toolTip or toolTipKey is set => get the string and put it into the ColumnInfo
			String toolTipKey = getToolTipKey();
			columnInfo.setToolTip((toolTipKey == null) ? getToolTip() : config.getMessageSource().getMessage(toolTipKey,
				null, toolTipKey, locale));
			columnInfo.setNestedList(this.nestedColumnInfoList);
			rowTag.addColumnInfo(columnInfo);
		}
		DisplayProvider displayProvider = rowTag.getDisplayProvider();
		StringBuffer sb = new StringBuffer(displayProvider.getCellPreProcess(getCellAttributes()));
		boolean hasBodyContent = false;
		if (displayProvider.doesIncludeBodyContent()
					&& bodyContent != null
					&& bodyContent.getString() != null
					&& bodyContent.getString().trim().length() > 0) {
			sb.append(bodyContent.getString().trim());
			bodyContent.clearBody();
			hasBodyContent = true;
		}
		{
			if (property != null && rowTag.getBeanName() != null) {
				try {
					Object bean = pageContext.getAttribute(rowTag.getBeanName());
					if (bean != null) {
						Object value = null;
						try {
							value = PropertyUtils.getProperty(bean, property);
						} catch (Exception e) {
							// Do nothing, if you want to handle this exception, then
							// use a try catch in the body content.
							LOGGER.error("<vlh:column> Error getting property='"
										+ property
										+ "' from the iterated JavaBean name='"
										+ rowTag.getBeanName()
										+ "'\n The row's JavaBean was >>>"
										+ bean
										+ "<<<\n Check the syntax or the spelling of the column's property!");
						}
						if (value != null) {
							if (sum != null && value instanceof Number) {
								double doubleValue = ((Number) value).doubleValue();
								Double sumValue = (Double) pageContext.getAttribute(sum);
								if (sumValue == null) {
									sumValue = new Double(doubleValue);
								} else {
									sumValue = new Double(sumValue.doubleValue() + doubleValue);
								}
								pageContext.setAttribute(sum, sumValue);
							}
							if (!hasBodyContent) {
								String formattedValue = JspUtils.format(value, format, locale);
								if (groupKey == null
											|| (config.getCellInterceptor() == null || !config.getCellInterceptor().isHidden(
												pageContext, groupKey, property, formattedValue))) {
									sb.append(displayProvider.emphase(formattedValue, getEmphasisPattern(),
										getColumnStyleClass()));
								}
							}
						} else if (!hasBodyContent) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("The property '" + property + "' of the iterated JavaBean '" + bean + "' is null!");
							}
							Object nullValue = (defaultValue == null) ? config.getNullToken() : defaultValue;
							if (groupKey == null
										|| (config.getCellInterceptor() == null || !config.getCellInterceptor().isHidden(
											pageContext, groupKey, property, nullValue))) {
								sb.append(nullValue);
							}
						}
					}
				} catch (Exception e) {
					final String message = "DefaultColumnTag.doEndTag() - <vlh:column> error getting property: "
								+ property
								+ " from bean.";
					LOGGER.error(message, e);
					throw new JspException(message, e);
				}
			}
		}
		sb.append(displayProvider.getCellPostProcess());
		JspUtils.write(pageContext, sb.toString());
		release();
		return EVAL_PAGE;
	}

	public String getColumnStyleClass() throws JspException {
		ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		ValueListConfigBean config = rootTag.getConfig();
		if (config == null) {
			throw new JspException("No config found on root tag");
		}
		return config.getStylePrefix();
	}

	/**
	 * Sets the defaultSort property.
	 * 
	 * @param value Valid values are "asc" and "desc".
	 */
	public void setSortable(String value) {
		if ("asc".equals(value)) {
			defaultSort = ValueListInfo.ASCENDING;
		} else if ("desc".equals(value)) {
			defaultSort = ValueListInfo.DESCENDING;
		}
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return this.property;
	}

	/**
	 * Sets the javabean property name of this column used to get data from list returned by adapter.
	 * 
	 * @param property The javabean property name of this column.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return this.format;
	}

	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return Returns the sum.
	 */
	public String getSum() {
		return this.sum;
	}

	/**
	 * @param sum The sum to set.
	 */
	public void setSum(String sum) {
		this.sum = sum;
	}

	/**
	 * @return Returns the defaultValue.
	 */
	public String getDefault() {
		return this.defaultValue;
	}

	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Returns the emphasisPattern.
	 */
	public String getEmphasisPattern() {
		return this.emphasisPattern;
	}

	/**
	 * It is used to emphasis part of text part of content of the displayed table cell. if
	 * <i>emphasisPattern</i> is empty("") sets it to null.
	 * 
	 * @param emphasisPattern The emphasisPattern to set.
	 */
	public void setEmphasisPattern(String emphasisPattern) {
		this.emphasisPattern = (emphasisPattern == null || emphasisPattern.length() == 0) ? null : emphasisPattern;
	}

	/**
	 * @param locale The locale to set for column.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return Returns the groupKey.
	 */
	public String getGroupKey() {
		return this.groupKey;
	}

	/**
	 * @param groupKey The groupKey to set.
	 */
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	/**
	 * @return Returns the nestedColumnInfoList.
	 */
	public List<ColumnInfo> getNestedColumnInfoList() {
		return nestedColumnInfoList;
	}

	/**
	 * @param nestedColumnInfoList The nestedColumnInfoList to set.
	 */
	public void setNestedColumnInfoList(List<ColumnInfo> nestedColumnInfoList) {
		this.nestedColumnInfoList = nestedColumnInfoList;
	}

	private void reset() {
		this.adapterProperty = null;
		this.defaultSort = null;
		this.defaultValue = null;
		this.emphasisPattern = null;
		this.format = null;
		this.groupKey = null;
		this.locale = null;
		this.property = null;
		this.sum = null;
		this.nestedColumnInfoList = null;
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