package org.webframe.web.page.web.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Abstracts the means by which text is displayed.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2004/07/26 12:56:21 $
 *  
 */
public interface DisplayHelper
{
   /**
    * Abstracts the means by which text is displayed.
    * 
    * @param pageContext
    *           The jsp PageContext
    * @param key
    *           The key, which could be the value to display or some sort of key
    *           to the text value.
    * 
    * @throws JspException
    *            Thrown by underling implementation.
    * 
    * @return The text value.
    */
   public abstract String help(PageContext pageContext, String key) throws JspException;
}