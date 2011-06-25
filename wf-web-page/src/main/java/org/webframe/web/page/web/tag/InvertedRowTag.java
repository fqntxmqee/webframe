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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.DefaultListBackedValueList;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.web.tag.support.ColumnInfo;
import org.webframe.web.page.web.tag.support.DisplayProvider;
import org.webframe.web.page.web.util.JspUtils;

/**
 * AAA add locale for format
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.12 $ $Date: 2005/11/23 14:55:49 $
 */
public class InvertedRowTag extends DefaultRowTag {

	private static final long						serialVersionUID	= 8608872950341954907L;

	private static final Log						LOGGER				= LogFactory.getLog(InvertedRowTag.class);

	private static final String					DEFAULT_FORMAT		= "0.00";

	private Map<Object, Map<Object, Object>>	yAxisMap				= new LinkedHashMap<Object, Map<Object, Object>>();

	private Map<Object, Object>					xAxisMap				= new LinkedHashMap<Object, Object>();

	private String										title;

	private String										format				= DEFAULT_FORMAT;

	public void convertValueList() throws JspException {
		ValueList vl = getRootTag().getValueList();
		try {
			while (vl.hasNext()) {
				Object bean = vl.next();
				Object xAxis = PropertyUtils.getProperty(bean, "ixaxis");
				Object yAxis = PropertyUtils.getProperty(bean, "iyaxis");
				Object value = PropertyUtils.getProperty(bean, "ivalue");
				xAxisMap.put(xAxis, null);
				Map<Object, Object> map = yAxisMap.get(yAxis);
				if (map == null) {
					yAxisMap.put(yAxis, map = new HashMap<Object, Object>());
					map.put("yaxis", yAxis);
				}
				map.put(JspUtils.format(xAxis, null, null).toLowerCase().replace(' ', '_'), value);
			}
		} catch (Exception e) {
			LOGGER.error("InvertedRowTag.convertValueList() exception...", e);
		}
		// Add all the columns to the tag context.
		for (Iterator<Object> iter = xAxisMap.keySet().iterator(); iter.hasNext();) {
			String label = JspUtils.format(iter.next(), null, null);
			addColumnInfo(new ColumnInfo(label, label.toLowerCase().replace(' ', '_'), null, null));
		}
		getRootTag().setValueList(
			new DefaultListBackedValueList(new ArrayList<Object>(yAxisMap.values()), vl.getValueListInfo()));
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		init();
		convertValueList();
		return super.doStartTag();
	}

	/**
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 * @todo figure out why release is not working.
	 */
	@SuppressWarnings("unchecked")
	public int doAfterBody() throws JspException {
		DisplayProvider displayProvider = getDisplayProvider();
		// If this is the first row, then print the column headers!
		if (currentRowNumber == 0) {
			JspUtils.writePrevious(pageContext, displayProvider.getHeaderRowPreProcess());
			JspUtils.writePrevious(pageContext, displayProvider.getHeaderCellPreProcess(null, null)
						+ title
						+ displayProvider.getHeaderCellPostProcess());
			for (Iterator<ColumnInfo> iter = getColumns().iterator(); iter.hasNext();) {
				ColumnInfo info = iter.next();
				JspUtils.writePrevious(pageContext, displayProvider.getHeaderCellPreProcess(null, null));
				JspUtils.writePrevious(pageContext, info.getTitle());
				JspUtils.writePrevious(pageContext, displayProvider.getHeaderCellPostProcess());
			}
			JspUtils.writePrevious(pageContext, displayProvider.getHeaderRowPostProcess());
			getColumns().clear();
		}
		if (beanInPageScope != null) {
			Map<String, Object> bean = (Map<String, Object>) beanInPageScope;
			String style = getRowStyleClass();
			pageContext.setAttribute(bean + "Style", style);
			appendClassCellAttribute(style);
			JspUtils.writePrevious(pageContext, displayProvider.getRowPreProcess(getCellAttributes()));
			// Add all the columns to the tag context.
			JspUtils.writePrevious(pageContext, displayProvider.getCellPreProcess(null)
						+ bean.get("yaxis")
						+ displayProvider.getCellPostProcess());
			for (Iterator<Object> iter = xAxisMap.keySet().iterator(); iter.hasNext();) {
				String label = JspUtils.format(iter.next(), null, null).toLowerCase().replace(' ', '_');
				JspUtils.writePrevious(pageContext, displayProvider.getCellPreProcess(null));
				if (bean.get(label) != null) {
					JspUtils.writePrevious(pageContext, JspUtils.format(bean.get(label), format, null));
				} else {
					JspUtils.writePrevious(pageContext, getRootTag().getConfig().getNullToken());
				}
				JspUtils.writePrevious(pageContext, displayProvider.getCellPostProcess());
			}
			JspUtils.writePrevious(pageContext, displayProvider.getRowPostProcess());
			bodyContent.clearBody();
		}
		currentRowNumber++;
		return processIteration();
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		reset();
		return result;
	}

	private void reset() {
		this.format = DEFAULT_FORMAT;
		this.title = null;
		this.xAxisMap.clear();
		this.yAxisMap.clear();
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 * @todo Create clean up method, call it at doEndTag, check whether is needed to be called in
	 *       processIteration insteed of release method.
	 */
	public void release() {
		super.release();
		reset();
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}