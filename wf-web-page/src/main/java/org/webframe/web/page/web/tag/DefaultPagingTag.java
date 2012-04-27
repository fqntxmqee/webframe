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

package org.webframe.web.page.web.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListConfigBean;
import org.webframe.web.page.web.util.DisplayHelper;
import org.webframe.web.page.web.util.JspUtils;

/**
 * Generate buttons to navigate through pages of data using i18n (internationalization). The
 * following keys are required to be define in message sources. <p> If you like to, you can add your
 * properties file in your locale and add this lines of code in your language: </p> Summary info:
 * <ol> <code> <li>paging.text.totalRow={0} Total </li> <li>paging.text.pageFromTotal= <b>{0}</b> of
 * {1} page(s) </li> </code> </ol> Paging info: <ol> <li>paging.first(off), paging.first(on)</li>
 * <li>paging.previous(off), paging.previous(on)</li> <li>paging.forward(off),
 * paging.forward(on)</li> <li>paging.last(off), paging.last(on)</li> <li>paging.delim</li>
 * <li>paging.text.totalRow</li> <li>paging.text.pageFromTotal</li> </ol> Focus info: <ol>
 * <li>paging.focus(on), paging.focus(off), paging.focus(disabled), paging.focus(error)</li> </ol>
 * Items per page info: <ol> <code> <li>paging.itemsPerPage.label = Items Per Page:</li>
 * <li>paging.itemsPerPage.title = Number of items per page.</li> <li>paging.itemsPerPage.button =
 * Set</li> </code> </ol>
 * 
 * @todo Document this tag. AAA separate summary to differnt tag, find better pictures for
 *       paging.focus
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.38 $ $Date: 2005/11/23 15:02:16 $
 */
public class DefaultPagingTag extends ConfigurableTag {

	/**
	 * 
	 */
	private static final long		serialVersionUID					= 3477630240958856509L;

	/**
	 * Pattern for ...border=...
	 */
	private static final Pattern	BORDER_ATTRIBUTE_PATTERN		= Pattern.compile("\\b(border\\s*=\\s*\\\"?\'?)",
																						Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern for ...cellspacing=...
	 */
	private static final Pattern	CELLSPACING_ATTRIBUTE_PATTERN	= Pattern.compile("\\b(cellspacing\\s*=\\s*\\\"?\'?)",
																						Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern for ...cellpadding=...
	 */
	private static final Pattern	CELLPADDING_ATTRIBUTE_PATTERN	= Pattern.compile("\\b(cellpadding\\s*=\\s*\\\"?\'?)",
																						Pattern.CASE_INSENSITIVE);

	private static final String	PAGING_TAG_STYLE					= "PagingTag";

	private static final String	SUMMARY_STYLE						= "Summary";

	private static final String	ITEMS_PER_PAGE_STYLE				= "ItemsPerPage";

	private static final String	INPUT_BOX_STYLE					= ITEMS_PER_PAGE_STYLE + "InputBox";

	private static final String	LABEL_STYLE							= ITEMS_PER_PAGE_STYLE + "Label";

	private static final String	SUBMIT_BUTTON_STYLE				= ITEMS_PER_PAGE_STYLE + "SubmitButton";

	private static final String	PAGING_STYLE						= "Paging";

	/* Tag attributes */
	private int							pages									= 0;

	private boolean					showSummary							= false;

	private boolean					showItemsPerPage					= false;

	private boolean					generateItemsPerPageForm		= true;

	/* State variables */
	private int							currentPage							= 0;

	private int							maxPage								= 0;

	private Map<String, Object>	parameters;

	private ValueListSpaceTag		rootTag;

	private DisplayHelper			displayHelper;

	private MessageSource			messageSource;

	private Locale						currentLocale;

	protected ValueListConfigBean getConfig() {
		return rootTag.getConfig();
	}

	protected ValueList<?> getValueList() {
		return rootTag.getValueList();
	}

	protected ValueListInfo getValueListInfo() {
		return rootTag.getValueList().getValueListInfo();
	}

	protected TableInfo getTableInfo() {
		return rootTag.getTableInfo();
	}

	protected Locale getLocale() {
		return this.currentLocale;
	}

	protected void init(HttpServletRequest request) throws JspException {
		this.rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		ValueListConfigBean config = rootTag.getConfig();
		this.displayHelper = config.getDisplayHelper();
		this.messageSource = config.getMessageSource();
		this.currentLocale = config.getLocale(request);
	}

	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		init((HttpServletRequest) pageContext.getRequest());
		Locale locale = getLocale();
		TableInfo tableInfo = getTableInfo();
		ValueListInfo valueListInfo = getValueListInfo();
		// Create a map of parameters that are used to generate the links.
		parameters = new HashMap<String, Object>(tableInfo.getParameters());
		parameters.put(ValueListInfo.SORT_COLUMN + tableInfo.getId(), valueListInfo.getSortingColumn());
		parameters.put(ValueListInfo.SORT_DIRECTION + tableInfo.getId(), valueListInfo.getSortingDirection());
		int page = valueListInfo.getPagingPage();
		int numberOfPages = valueListInfo.getTotalNumberOfPages();
		this.currentPage = page - (pages / 2);
		if (this.currentPage < 1) {
			this.currentPage = 1;
		}
		this.maxPage = (this.currentPage - 1) + pages;
		if (this.maxPage > numberOfPages) {
			this.currentPage -= (this.maxPage - numberOfPages);
			this.maxPage = numberOfPages;
		}
		if (this.maxPage < 2) {
			this.maxPage = 0;
		}
		if (this.currentPage < 1) {
			this.currentPage = 1;
		}
		StringBuffer sb = new StringBuffer();
		final String stylePrefix = getConfig().getStylePrefix();
		final String pagingTagStyle = stylePrefix + PAGING_TAG_STYLE + " " + PAGING_TAG_STYLE;
		final String summaryStyle = stylePrefix + SUMMARY_STYLE + " " + SUMMARY_STYLE;
		final String itemsPerPageStyle = stylePrefix + ITEMS_PER_PAGE_STYLE + " " + ITEMS_PER_PAGE_STYLE;
		final String pagingStyle = stylePrefix + PAGING_STYLE + " " + PAGING_STYLE;
		sb.append("<table class='").append(pagingTagStyle).append("'");
		String attributes = getAttributes();
		if (attributes != null) {
			if (!BORDER_ATTRIBUTE_PATTERN.matcher(attributes).find()) {
				sb.append(" border=0");
			}
			if (!CELLSPACING_ATTRIBUTE_PATTERN.matcher(attributes).find()) {
				sb.append(" cellspacing=0");
			}
			if (!CELLPADDING_ATTRIBUTE_PATTERN.matcher(attributes).find()) {
				sb.append(" cellpadding=0");
			}
			sb.append(" ").append(attributes);
		}
		sb.append(">");
		sb.append("\n <tr class='").append(pagingTagStyle).append("'>");
		if (showSummary) {
			sb.append("\n  <td class='").append(summaryStyle).append("'>");
			renderSumary(sb);
			sb.append("</td>");
			if (showItemsPerPage) {
				HashMap<String, Object> itemsPerPageParameters = new HashMap<String, Object>(parameters);
				sb.append("\n  <td class='").append(itemsPerPageStyle).append("'>");
				renderItemsPerPage(sb, itemsPerPageParameters);
				sb.append("</td>");
			}
			sb.append("\n  <td class='").append(pagingStyle).append("'>");
			sb.append("<table border='0' cellspacing='0' cellpadding='0' class='").append(pagingStyle).append("'>");
			sb.append("\n    <tr class='").append(pagingStyle).append("'>");
		}
		parameters.put(ValueListInfo.PAGING_NUMBER_PER + tableInfo.getId(),
			String.valueOf(valueListInfo.getPagingNumberPer()));
		renderFocusControl(sb);
		String value;
		if (page > 1) {
			parameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(), "1");
			value = getMessage("paging.first(on)", null, locale);
			sb.append("\n     <td>");
			renderPagingLink(sb, parameters, value);
			sb.append("</td>");
			String delim = getMessage("paging.delim", null, "", locale);
			if (value.length() > 0 && delim.length() > 0) {
				sb.append("\n     <td>").append(delim).append("</td>");
			}
			parameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(), String.valueOf(page - 1));
			value = getMessage("paging.previous(on)", null, locale);
			sb.append("\n     <td>");
			renderPagingLink(sb, parameters, value);
			sb.append("</td>");
			if (value.length() > 0 && delim.length() > 0) {
				sb.append("\n     <td>").append(delim).append("</td>");
			}
		} else {
			sb.append("\n     <td>").append(getMessage("paging.first(off)", null, locale)).append("</td>");
			sb.append("\n     <td>").append(getMessage("paging.previous(off)", null, locale)).append("</td>");
		}
		JspUtils.write(pageContext, sb.toString());
		pageContext.setAttribute("page" + tableInfo.getId(), new Integer(this.currentPage));
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * Try to resolve the message. Treat as an error if the message can't be found.
	 * 
	 * @param code the code to lookup up, such as 'calculator.noRateSet'
	 * @param args Array of arguments that will be filled in for params within the message (params
	 *           look like "{0}", "{1,date}", "{2,time}" within a message), or null if none.
	 * @param locale the Locale in which to do the lookup
	 * @return the resolved message
	 * @throws NoSuchMessageException if the message wasn't found
	 * @see <a
	 *      href="http://java.sun.com/j2se/1.3/docs/api/java/text/MessageFormat.html">java.text.MessageFormat</a>
	 */
	protected String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException, JspException {
		return displayHelper.help(pageContext, messageSource.getMessage(code, args, locale));
	}

	/**
	 * Try to resolve the message. Return default message if no message was found.
	 * 
	 * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of this class are
	 *           encouraged to base message names on the relevant fully qualified class name, thus
	 *           avoiding conflict and ensuring maximum clarity.
	 * @param args array of arguments that will be filled in for params within the message (params
	 *           look like "{0}", "{1,date}", "{2,time}" within a message), or null if none.
	 * @param locale the Locale in which to do the lookup
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message if the lookup was successful; otherwise the default message
	 *         passed as a parameter
	 * @see <a
	 *      href="http://java.sun.com/j2se/1.3/docs/api/java/text/MessageFormat.html">java.text.MessageFormat</a>
	 */
	protected String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
				throws NoSuchMessageException, JspException {
		return displayHelper.help(pageContext, messageSource.getMessage(code, args, defaultMessage, locale));
	}

	/**
	 * @param sb
	 * @param itemsPerPageParameters
	 * @throws NoSuchMessageException
	 * @throws JspException
	 * @todo DO IT WIHTOUT JAVASCRIPT!
	 */
	protected void renderItemsPerPage(StringBuffer sb, HashMap<String, Object> itemsPerPageParameters)
				throws NoSuchMessageException, JspException {
		Locale locale = getLocale();
		ValueListInfo valueListInfo = getValueListInfo();
		TableInfo tableInfo = getTableInfo();
		itemsPerPageParameters.remove(ValueListInfo.PAGING_NUMBER_PER + tableInfo.getId());
		itemsPerPageParameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(),
			String.valueOf(valueListInfo.getPagingPage()));
		if (valueListInfo.isFocusEnabled()) {
			itemsPerPageParameters.put(ValueListInfo.DO_FOCUS + tableInfo.getId(), valueListInfo.isDoFocusAgain()
						? "true"
						: "false");
			if (valueListInfo.getFocusProperty() != null) {
				itemsPerPageParameters.put(ValueListInfo.FOCUS_PROPERTY + tableInfo.getId(),
					valueListInfo.getFocusProperty());
			}
			if (valueListInfo.getFocusValue() != null) {
				itemsPerPageParameters.put(ValueListInfo.FOCUS_VALUE + tableInfo.getId(), valueListInfo.getFocusValue());
			}
		}
		String propertyName = ValueListInfo.PAGING_NUMBER_PER + tableInfo.getId();
		String delim = getMessage("paging.delim", null, "", locale);
		sb.append(delim);
		final String stylePrefix = getConfig().getStylePrefix();
		final String commonStyle = stylePrefix + ITEMS_PER_PAGE_STYLE + " " + ITEMS_PER_PAGE_STYLE;
		final String labelStyle = stylePrefix + LABEL_STYLE + " " + LABEL_STYLE;
		final String inputBoxStyle = stylePrefix + INPUT_BOX_STYLE + " " + INPUT_BOX_STYLE;
		final String submitButtonStyle = stylePrefix + SUBMIT_BUTTON_STYLE + " " + SUBMIT_BUTTON_STYLE;
		if (isGenerateItemsPerPageForm()) {
			sb.append("<form action='#' onsubmit='return false;' class='").append(commonStyle).append("'>");
		}
		sb.append("<table border='0' cellspacing='0' cellpadding='0' class='").append(commonStyle).append("'>");
		sb.append("\n    <tr class='").append(commonStyle).append("'>");
		Map<String, Object> labelAttrs = new HashMap<String, Object>();
		labelAttrs.put("class", labelStyle);
		sb.append("\n     <td nowrap='true' class='").append(labelStyle).append("'>");
		renderPerPageLabel(sb, labelAttrs, getMessage("paging.itemsPerPage.label", null, locale));
		sb.append("</td>");
		Map<String, Object> inputBoxAttrs = new HashMap<String, Object>();
		inputBoxAttrs.put("id", propertyName);
		inputBoxAttrs.put("name", propertyName);
		inputBoxAttrs.put("class", inputBoxStyle);
		inputBoxAttrs.put("title", getMessage("paging.itemsPerPage.title", null, locale));
		inputBoxAttrs.put("value", new Integer(valueListInfo.getPagingNumberPer()));
		sb.append("\n     <td class='").append(inputBoxStyle).append("'>");
		renderPerPageInputBox(sb, inputBoxAttrs);
		sb.append("</td>");
		Map<String, Object> submitButtonAttrs = new HashMap<String, Object>();
		submitButtonAttrs.put("class", submitButtonStyle);
		submitButtonAttrs.put("value", getMessage("paging.itemsPerPage.button", null, locale));
		submitButtonAttrs.put("onclick", "window.location.href = \""
					+ tableInfo.getUrl()
					+ getConfig().getLinkEncoder().encode(pageContext, itemsPerPageParameters)
					+ propertyName
					+ "=\" + document.getElementById(\""
					+ propertyName
					+ "\").value;");
		sb.append("\n     <td class='").append(submitButtonStyle).append("'>");
		renderPerPageSubmitButton(sb, submitButtonAttrs);
		sb.append("</td>");
		sb.append("\n    </tr>");
		sb.append("\n  </table>");
		if (isGenerateItemsPerPageForm()) {
			sb.append("</form>");
		}
	}

	/**
	 * Render label for items per page input box. Subclasses can overide or extend the method to
	 * provide different behaviour.
	 * 
	 * @param sb StringBuffer to render into
	 * @param attributes
	 * @param text
	 */
	protected void renderPerPageLabel(StringBuffer sb, Map<String, Object> attributes, String text) {
		renderBodyTag(sb, "label", attributes, text);
	}

	/**
	 * Render input box for items per page. Subclasses can overide or extend the method to provide
	 * different behaviour.
	 * 
	 * @param sb StringBuffer to tender into
	 * @param attributes Attributes for rendering a tag
	 */
	protected void renderPerPageInputBox(StringBuffer sb, Map<String, Object> attributes) {
		attributes.put("type", "text");
		renderSimpleTag(sb, "input", attributes);
	}

	/**
	 * Render submit button for items per page input box. Subclasses can overide or extend the method
	 * to provide different behaviour.
	 * 
	 * @param sb
	 * @param attributes
	 */
	protected void renderPerPageSubmitButton(StringBuffer sb, Map<String, Object> attributes) {
		if (!attributes.containsKey("type")) {
			attributes.put("type", "button");
		}
		renderSimpleTag(sb, "input", attributes);
	}

	private static void renderSimpleTag(StringBuffer sb, String name, Map<String, Object> attributes) {
		sb.append("<").append(name);
		if (attributes != null) {
			Iterator<Entry<String, Object>> i = attributes.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, Object> entry = i.next();
				sb.append(" ").append(entry.getKey()).append("='").append(entry.getValue()).append("'");
			}
		}
		sb.append(">");
	}

	private static void renderBodyTag(StringBuffer sb, String name, Map<String, Object> attributes, String content) {
		renderSimpleTag(sb, name, attributes);
		if (content != null) {
			sb.append(content);
		}
		sb.append("</").append(name).append(">");
	}

	/**
	 * @param sb
	 * @throws JspException
	 * @throws NoSuchMessageException
	 */
	protected void renderSumary(StringBuffer sb) throws NoSuchMessageException, JspException {
		Locale locale = getLocale();
		ValueListInfo valueListInfo = getValueListInfo();
		sb.append(getMessage("paging.text.totalRow", new Object[]{
			new Integer(valueListInfo.getTotalNumberOfEntries())}, locale));
		sb.append(getMessage("paging.text.pageFromTotal", new Object[]{
					new Integer(valueListInfo.getPagingPage()), new Integer(valueListInfo.getTotalNumberOfPages())}, locale));
	}

	/**
	 * @param sb
	 * @throws JspException
	 */
	protected void renderFocusControl(StringBuffer sb) throws JspException {
		Locale locale = getLocale();
		ValueListInfo valueListInfo = getValueListInfo();
		TableInfo tableInfo = getTableInfo();
		String value;
		if (valueListInfo.isFocusEnabled()) {
			parameters.put(ValueListInfo.FOCUS_PROPERTY + tableInfo.getId(), valueListInfo.getFocusProperty());
			if (valueListInfo.getFocusValue() != null) {
				parameters.put(ValueListInfo.FOCUS_VALUE + tableInfo.getId(), valueListInfo.getFocusValue());
			}
			// AAA focus error behavier
			HashMap<String, Object> focusParameters = new HashMap<String, Object>(parameters);
			if (valueListInfo.getFocusStatus() != ValueListInfo.FOCUS_TOO_MANY_ITEMS) {
				focusParameters.put(ValueListInfo.DO_FOCUS + tableInfo.getId(), valueListInfo.isDoFocusAgain()
							? "false"
							: "true");
				value = getMessage(valueListInfo.isDoFocusAgain() ? "paging.focus(off)" : "paging.focus(on)", null, locale);
				sb.append("\n     <td>");
				renderPagingLink(sb, parameters, value);
				sb.append("</td>");
				String delim = getMessage("paging.delim", null, "", locale);
				if (value.length() > 0 && delim.length() > 0) {
					sb.append("\n     <td>").append(delim).append("</td>");
				}
			} else {
				if (valueListInfo.isFocusEnabled()) {
					sb.append("\n     <td>").append(getMessage("paging.focus(error)", null, locale)).append("</td>");
				}
			}
		} else {
			sb.append("\n     <td>").append(getMessage("paging.focus(disabled)", null, locale)).append("</td>");
		}
	}

	/**
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	@Override
	public int doAfterBody() throws JspException {
		// method doAfterBody() is always invoked on WebLogic (even if the tag has no body)
		if (this.currentPage <= this.maxPage) {
			TableInfo tableInfo = getTableInfo();
			String label = null;
			if (getBodyContent() != null) {
				label = getBodyContent().getString();
				if (label != null) {
					label = label.trim();
				}
				getBodyContent().clearBody();
			}
			if (label == null || label.length() == 0) {
				return SKIP_BODY;
			}
			StringBuffer sb = new StringBuffer();
			renderContent(sb, label);
			JspUtils.writePrevious(pageContext, sb.toString());
			pageContext.setAttribute("page" + tableInfo.getId(), new Integer(++this.currentPage));
			return EVAL_BODY_AGAIN;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * @param sb
	 * @param label
	 */
	private void renderContent(StringBuffer sb, String label) {
		ValueListInfo valueListInfo = getValueListInfo();
		TableInfo tableInfo = getTableInfo();
		if (this.currentPage == valueListInfo.getPagingPage()) {
			sb.append("\n     <th>").append(label).append("</th>");
		} else {
			parameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(), String.valueOf(this.currentPage));
			sb.append("\n     <td>");
			renderPagingLink(sb, parameters, label);
			sb.append("</td>");
		}
	}

	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		Locale locale = getLocale();
		String delim = getMessage("paging.delim", null, locale);
		ValueListInfo valueListInfo = getValueListInfo();
		TableInfo tableInfo = getTableInfo();
		int page = valueListInfo.getPagingPage();
		int numberOfPages = valueListInfo.getTotalNumberOfPages();
		if (getBodyContent() == null
					|| getBodyContent().getString() == null
					|| getBodyContent().getString().trim().length() == 0) {
			while (this.currentPage <= this.maxPage) {
				renderContent(sb, String.valueOf(this.currentPage));
				this.currentPage++;
			}
		}
		if (page < numberOfPages) {
			parameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(), String.valueOf(page + 1));
			String value = getMessage("paging.forward(on)", null, locale);
			sb.append("\n     <td>");
			renderPagingLink(sb, parameters, value);
			sb.append("</td>");
			if (value.length() > 0 && delim.length() > 0) {
				sb.append("\n     <td>").append(delim).append("</td>");
			}
			parameters.put(ValueListInfo.PAGING_PAGE + tableInfo.getId(), String.valueOf(numberOfPages));
			value = getMessage("paging.last(on)", null, locale);
			sb.append("\n     <td>");
			renderPagingLink(sb, parameters, value);
			sb.append("</td>");
		} else {
			sb.append("\n     <td>").append(getMessage("paging.forward(off)", null, locale)).append("</td>");
			sb.append("\n     <td>").append(getMessage("paging.last(off)", null, locale)).append("</td>");
		}
		sb.append("\n    </tr>");
		sb.append("\n   </table>");
		if (showSummary) {
			sb.append("</td>");
			sb.append("\n </tr>");
			sb.append("\n</table>");
		}
		JspUtils.write(pageContext, sb.toString());
		release();
		return EVAL_PAGE;
	}

	/**
	 * Renders <code>&lt;a&gt;</code> tag for control buttons. Subclasses can overide or extend the
	 * method to provide different behaviour.
	 * 
	 * @param sb StringBuffer to render into
	 * @param parameters Map of parameters
	 * @param value Label
	 */
	protected void renderPagingLink(StringBuffer sb, Map<String, Object> parameters, String value) {
		TableInfo tableInfo = getTableInfo();
		sb.append("<a href=\"").append(tableInfo.getUrl());
		sb.append(rootTag.getConfig().getLinkEncoder().encode(pageContext, parameters));
		sb.append("\">").append(value).append("</a>");
	}

	/**
	 * @return Returns the pages.
	 */
	public int getPages() {
		return this.pages;
	}

	/**
	 * @param pages The pages to set.
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * @return showSummary
	 */
	public boolean isShowSummary() {
		return showSummary;
	}

	/**
	 * @param showSummary The showSummary to set.
	 */
	public void setShowSummary(boolean showSummary) {
		this.showSummary = showSummary;
	}

	/**
	 * @return showItemsPerPage
	 */
	public boolean isShowItemsPerPage() {
		return showItemsPerPage;
	}

	/**
	 * Enable or disable generating form for number of items per page.
	 * 
	 * @param showItemsPerPage
	 */
	public void setShowItemsPerPage(boolean showItemsPerPage) {
		this.showItemsPerPage = showItemsPerPage;
	}

	/**
	 * @return Returns the generateItemsPerPageForm.
	 */
	public boolean isGenerateItemsPerPageForm() {
		return generateItemsPerPageForm;
	}

	/**
	 * Enable or disable generating of HTML form tag wrapping input box "items per page".
	 * 
	 * @param generateItemsPerPageForm The generateItemsPerPageForm to set.
	 */
	/**
	 * @param nestedForm
	 */
	public void setGenerateItemsPerPageForm(boolean nestedForm) {
		this.generateItemsPerPageForm = nestedForm;
	}

	private void reset() {
		this.rootTag = null;
		this.currentPage = 0;
		this.generateItemsPerPageForm = true;
		this.maxPage = 0;
		this.pages = 0;
		this.parameters = null;
		this.showItemsPerPage = false;
		this.showSummary = false;
		this.displayHelper = null;
		this.messageSource = null;
		this.currentLocale = null;
	}

	/**
	 * Called on a Tag handler to release state. The page compiler guarantees that JSP page
	 * implementation objects will invoke this method on all tag handlers, but there may be multiple
	 * invocations on doStartTag and doEndTag in between.
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	@Override
	public void release() {
		super.release();
		reset();
	}
}