
package org.webframe.web.page.web.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.TagUtils;

/**
 * The StrutsDisplayHelper is used to i18n the column headers. It can be used by adding the folloing
 * in the spring config file: <pre> <bean id="myLook" singleton="true"
 * class="org.webframe.web.page.web.ValueListConfigBean"> <property name="displayHelper"> <bean
 * class="org.webframe.web.page.web.util.StrutsDisplayHelper" /> </property> ... </bean> </pre> This
 * DisplayHelper simply takes the value of the column and looks up the message in the default
 * resource bundle. NOTE: The prefered way to i18n is to use the titleKey attribute and the
 * messageSource defined in the ValueListConfigBean.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.5 $ $Date: 2005/10/20 16:38:08 $
 */
public class StrutsDisplayHelper implements DisplayHelper {

	/**
	 * @see org.webframe.web.page.web.util.DisplayHelper#help(javax.servlet.jsp.PageContext,
	 *      java.lang.String)
	 */
	public String help(PageContext pageContext, String key) throws JspException {
		return TagUtils.getInstance().message(pageContext, null, null, key);
	}
}