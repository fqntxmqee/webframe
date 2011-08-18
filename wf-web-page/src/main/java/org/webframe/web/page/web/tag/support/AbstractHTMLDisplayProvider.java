/*
 * Created on 21.2.2005
 * azachar
 */

package org.webframe.web.page.web.tag.support;

import org.webframe.web.page.ValueListInfo;

/**
 * AbstractHTMLDisplayProvider implement common parts for display providers.
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.2 $ $Date: 2005/11/23 14:37:14 $
 */
public abstract class AbstractHTMLDisplayProvider implements DisplayProvider {

	public boolean doesIncludeBodyContent() {
		return true;
	}

	/**
	 * @param String text
	 * @param String emphasisPattern
	 * @return Emphase text with emphasesis prefix and postfix. example: <a class='emphase'>
	 *         emphasisPattern </a>
	 */
	public String emphase(String text, String emphasisPattern, String style) {
		if (emphasisPattern == null) return text;
		StringBuffer replacement = new StringBuffer("<span class='" + style + "Emphase'>");
		replacement.append(emphasisPattern);
		replacement.append("</span>");
		return text.replaceAll(emphasisPattern, replacement.toString());
	}

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	public String getHeaderRowPostProcess() {
		return "</tr>";
	}

	/**
	 * Get the HTML that comes before the column text.
	 * 
	 * @return The HTML that comes before the column text.
	 */
	public String getHeaderRowPreProcess() {
		return "\n<tr>";
	}

	public String getMimeType() {
		return null;
	}

	public String getRowPostProcess() {
		return "</tr>";
	}

	public String getRowPreProcess(Attributes attributes) {
		return (attributes == null) ? "\n<tr>" : "\n<tr " + attributes.getCellAttributesAsString() + ">";
	}

	public String getNestedHeaderPreProcess(ColumnInfo columnInfo, ValueListInfo info) {
		return (columnInfo.getAttributes() == null) ? "<table width=\"100%\">" : "<table width=\"100%\" "
					+ columnInfo.getAttributes()
					+ ">";
	}

	public String getNestedHeaderPostProcess() {
		return "</table>";
	}
}
