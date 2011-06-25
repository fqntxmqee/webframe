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
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. >
 * http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.web.tag;

import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.webframe.web.page.DefaultListBackedValueList;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.web.ValueListConfigBean;
import org.webframe.web.page.web.ValueListRequestUtil;
import org.webframe.web.page.web.tag.support.HtmlDisplayProvider;
import org.webframe.web.page.web.tag.support.ParamAddable;
import org.webframe.web.page.web.tag.support.ValueListNullSpacer;
import org.webframe.web.page.web.util.ImagesHomeDisplayHelper;
import org.webframe.web.page.web.util.JspUtils;

/**
 * @author Matthew L. Wilson, Andrej Zachar, Luciano Cunha
 * @version $Revision: 1.29 $ $Date: 2005/11/23 14:47:51 $
 */
public class ValueListSpaceTag extends BodyTagSupport implements ParamAddable {

	private static final long		serialVersionUID	= 7795421339534088302L;

	/** Commons Logger */
	private static final Log		LOGGER				= LogFactory.getFactory().getInstance(ValueListSpaceTag.class);

	/**
	 * Parent root tag in case of nested valuelist
	 */
	private ValueListSpaceTag		parentRootTag;

	protected ValueListConfigBean	config;

	protected ValueList				valueList			= null;

	protected TableInfo				tableInfo			= new TableInfo(TableInfo.DEFAULT_ID);

	protected String					valueListName		= null;

	protected String					valueListScope		= null;

	/**
	 * @author Luciano Cunha
	 */
	protected String					excludeParameters;

	/**
	 * true - If is valueList set from mvc or from jsp via tag retrieve.
	 */
	private boolean					wasRetrieved		= false;

	/**
	 * Parent root tag in case of nested valuelist
	 * 
	 * @return parent root tag
	 */
	public ValueListSpaceTag getParentRootTag() {
		return parentRootTag;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		parentRootTag = (ValueListSpaceTag) findAncestorWithClass(this, ValueListSpaceTag.class);
		Object bean;
		if ("session".equals(valueListScope)) {
			bean = pageContext.getAttribute(valueListName, PageContext.SESSION_SCOPE);
		} else if ("application".equals(valueListScope)) {
			bean = pageContext.getAttribute(valueListName, PageContext.APPLICATION_SCOPE);
		} else {
			bean = pageContext.getAttribute(valueListName, PageContext.REQUEST_SCOPE);
		}
		if (bean != null) {
			if (bean instanceof ValueList) {
				setValueList((ValueList) bean);
			} else if (bean instanceof List) {
				setValueList(new DefaultListBackedValueList((List<Object>) bean));
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("List '" + valueListName + "' was wrapped with DefaultListBackedValueList.");
				}
			} else {
				String msg = "ValueList '" + valueListName + "' of of unknown type " + bean.getClass().getName() + "!";
				LOGGER.warn(msg);
			}
		} else {
			LOGGER.error("ValueList '"
						+ valueListName
						+ "'  was not found in the scope '"
						+ (valueListScope == null ? "request" : valueListScope)
						+ "'!");
		}
		if (bean != null) {
			pageContext.setAttribute(valueListName, bean);
		}
		tableInfo.setName(valueListName);
		tableInfo.setConfig(getConfig());
		tableInfo.setPageContext(pageContext);
		// TODO Do not swallow this!!!!!
		pageContext.setAttribute(
			ImagesHomeDisplayHelper.IMAGES_HOME_ATTRIBUTE_KEY,
			((HtmlDisplayProvider) tableInfo.getConfig().getDisplayProvider("html")).getImageHome((HttpServletRequest) pageContext.getRequest()));
		/**
		 * @author Luciano Cunha
		 */
		excludeParameters();
		return super.doStartTag();
	}

	/**
	 * Exclude parameters.
	 * 
	 * @author Luciano Cunha
	 */
	public void excludeParameters() {
		if (excludeParameters != null) {
			for (StringTokenizer st = new StringTokenizer(excludeParameters, "|"); st.hasMoreTokens();) {
				String key = st.nextToken();
				if (tableInfo.getParameters() != null && tableInfo.getParameters().containsKey(key)) {
					tableInfo.getParameters().remove(key);
				}
			}
		}
	}

	/**
	 * Write the bodyContent.
	 * 
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspException {
		if (bodyContent != null) {
			JspUtils.writePrevious(pageContext, bodyContent.getString());
		}
		valueList = null;
		valueListName = null;
		valueListScope = null;
		config = null;
		wasRetrieved = false;
		tableInfo = new TableInfo(TableInfo.DEFAULT_ID);
		return SKIP_BODY;
	}

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		reset();
		return result;
	}

	/**
	 * Setter for the base URL.
	 * 
	 * @param url New value of property url.
	 */
	public void setUrl(String url) {
		tableInfo.setUrl(url);
	}

	/**
	 * Setter for the included properties
	 * 
	 * @param forwardParams The included properties
	 */
	public void setIncludeParameters(String forwardParams) {
		tableInfo.getParameters().clear();
		tableInfo.getParameters().putAll(
			ValueListRequestUtil.getAllForwardParameters(pageContext, forwardParams, tableInfo.getId()));
	}

	/**
	 * Setter for the excluded properties
	 * 
	 * @param excludedParameters The excluded properties
	 * @autor Luciano Cunha
	 */
	public void setExcludeParameters(String excludedParameters) {
		this.excludeParameters = excludedParameters;
	}

	/**
	 * Sets the name of the ValueList
	 * 
	 * @param name The name to set of the ValueList.
	 */
	public void setValue(String name) {
		valueListName = name;
	}

	/**
	 * If is your retrieved ValueList null or you forgot to send a valueList it returns the
	 * ValueListNullSpacer.
	 * 
	 * @return Returns the valueList. NEVER RETURN NULL
	 */
	public ValueList getValueList() {
		if (valueList == null) {
			if (!wasRetrieved) {
				LOGGER.error("Please set any valueList before use! \n A) Check if you have the tag <vlh:retrieve .../>  before using data, if you are not using Controller! The most safe place is to put it after the root tag."
							+ "\n B) If you are using a Controller,  check if you set in the scope '"
							+ (valueListScope == null ? "request" : valueListScope)
							+ "' any valueList with the name '"
							+ valueListName
							+ "' \n C) Someone could hack parameters, no valueList was found.");
			} else {
				LOGGER.warn("Retrieved valueList is null!");
			}
			LOGGER.warn("Returning singleton ValueListNullSpacer!");
			valueList = ValueListNullSpacer.getInstance();
		}
		return valueList;
	}

	/**
	 * @param valueList The valueList to set.
	 */
	public void setValueList(ValueList valueList) {
		this.valueList = valueList;
		wasRetrieved = true;
	}

	/**
	 * @return Returns the tableInfo.
	 */
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		tableInfo.setId(id);
	}

	/**
	 * @return Returns the config.
	 */
	public ValueListConfigBean getConfig() {
		if (config == null) {
			LOGGER.warn("No ValueListConfigBean defined, using the default configuration.");
			config = new ValueListConfigBean();
		}
		return config;
	}

	/**
	 * @param configName The config to set.
	 */
	public void setConfigName(String configName) {
		try {
			WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			config = (ValueListConfigBean) context.getBean(configName, ValueListConfigBean.class);
		} catch (BeansException e) {
			config = null;
			LOGGER.error("Check spelling of the config='" + configName + "' Error: " + e.getMessage());
		}
	}

	/**
	 * @param valueListScope The valueListScope to set.
	 */
	public void setScope(String valueListScope) {
		this.valueListScope = valueListScope;
	}

	/**
	 * Add params to url.
	 * 
	 * @see org.webframe.web.page.web.tag.support.ParamAddable#addParam(java.lang.String,
	 *      java.lang.String)
	 */
	public void addParam(String key, String value) {
		tableInfo.getParameters().put(key, value);
	}

	private void reset() {
		this.parentRootTag = null;
		this.config = null;
		this.excludeParameters = null;
		this.tableInfo = new TableInfo(TableInfo.DEFAULT_ID);
		this.valueList = null;
		this.valueListName = null;
		this.valueListScope = null;
		this.wasRetrieved = false;
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