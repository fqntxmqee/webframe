/**
 * Copyright (c) 2003 held jointly by the individual authors. This library is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details. You should have received a copy of the GNU Lesser
 * General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA. > http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */

package org.webframe.web.page.web.tag;

import java.util.HashMap;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.web.page.web.tag.support.ParamAddable;
import org.webframe.web.page.web.util.JspUtils;

/**
 * This tag creates action. It is ment to be used along with ControlsTag </code> <pre>
 * &lt;vlh:action url=&quot;/delete.do?&quot; customParameters=&quot;
 * &lt;%=HashMapOfCustomParams%&gt;&quot;&gt; &lt;vlh:addParam name=&quot;rowId&quot;
 * property=&quot;id&quot; temp=&quot;true&quot;/&gt; &lt;vlh:addParam
 * name=&quot;rowStaticParamName&quot; value=&quot;CommonForAllRows&quot; temp=&quot;true&quot;/&gt;
 * Temp dynamic and static params. &lt;/vlh:action&gt; </pre></code>
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.9 $ $Date: 2005/11/23 14:51:53 $ $Author: smarek $
 */
public class ActionTag extends ConfigurableTag implements ParamAddable {

	private static final long			serialVersionUID				= -4765405639511065820L;

	/**
	 * Logger for this class
	 */
	private static final Log			LOGGER							= LogFactory.getLog(ActionTag.class);

	/**
	 * The url use to append all needed parameters, if not set rootTag url is used.
	 */
	private String							url								= null;

	/** Any custom parameters are appended to url. */
	private HashMap<String, Object>	customParameters				= null;

	/**
	 * Any row properties from nested AddParamTag are stored in actionParameters, which are appended
	 * to url.
	 */
	private HashMap<String, Object>	actionParameters				= new HashMap<String, Object>();

	/**
	 * This prefix is used to recognize, which url parameters are temporaly requested for an action.
	 * <li> Default is set as String <b>"ACT"</b> </li>
	 */
	public static final String			ACTION_TEMP_PARAM_PREFIX	= "ACT";

	public int doStartTag() throws JspException {
		actionParameters.clear();
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);
		StringBuffer sb = new StringBuffer("<a ");
		sb.append(getCellAttributes().getCellAttributesAsString());
		sb.append("href=\"");
		sb.append(getUrl(rootTag));
		sb.append(rootTag.getTableInfo().getConfig().getLinkEncoder().encode(pageContext, getAllParameters(rootTag)));
		sb.append("\">");
		sb.append(bodyContent.getString().trim());
		sb.append("</a>");
		JspUtils.write(pageContext, sb.toString());
		release();
		return EVAL_PAGE;
	}

	/**
	 * Make a final map of parameters for url encoder. At first ar added table parameters, then are
	 * added tableId with paramName ACTION_TEMP_PARAM_PREFIX, then customParams and finally are added
	 * actionParams.
	 */
	private HashMap<String, Object> getAllParameters(ValueListSpaceTag rootTag) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (rootTag.getTableInfo().getParameters() != null) {
			map.putAll(rootTag.getTableInfo().getParameters());
		}
		// if (rootTag.getTableInfo().getId() != TableInfo.DEFAULT_ID) {
		// map.put(ACTION_TEMP_PARAM_PREFIX, rootTag.getTableInfo().getId());
		// }
		if (customParameters != null) {
			map.putAll(customParameters);
		}
		// need to be last to overwrite the same params
		map.putAll(actionParameters);
		return map;
	}

	/**
	 * @param customParameters The customParameters to encode in the action's url.
	 */
	public void setCustomParameters(HashMap<String, Object> customParameters) {
		this.customParameters = customParameters;
	}

	/**
	 * @return String Url, if in tag is nothing setted, return base table url.
	 */
	private String getUrl(ValueListSpaceTag rootTag) {
		if (url == null) {
			return rootTag.getTableInfo().getUrl();
		} else {
			return url;
		}
	}

	/**
	 * @param url The base url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Add parameters to url for an action. <pre> url ? key = value &amp; </pre>
	 */
	public void addParam(String key, String value) {
		if (LOGGER.isDebugEnabled() && value == null) {
			LOGGER.debug("Do you really want to add param '" + key + "' which value is  null?");
		}
		actionParameters.put(key, value);
	}

	private void reset() {
		this.actionParameters.clear();
		this.customParameters = null;
		this.url = null;
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