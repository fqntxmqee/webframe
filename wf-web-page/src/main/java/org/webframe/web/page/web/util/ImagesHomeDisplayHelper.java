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
package org.webframe.web.page.web.util;

import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * The ImagesHomeDisplayHelper can be used by adding the folloing in the spring
 * config file:
 * 
 * <pre>
 * 
 *  
 *      &lt;bean id=&quot;myLook&quot; singleton=&quot;true&quot; class=&quot;org.webframe.web.page.web.ValueListConfigBean&quot;&gt;
 *        &lt;property name=&quot;displayHelper&quot;&gt;
 *           &lt;bean class=&quot;org.webframe.web.page.web.util.ImagesHomeDisplayHelper&quot; /&gt;
 *        &lt;/property&gt;
 *        ...
 *      &lt;/bean&gt;
 *    
 *  
 * </pre>
 * 
 * This ImagesHomeDisplayHelper simply takes the value of your key and replace
 * any occurrences of the text <code>@IMAGES_HOME@</code> with <b>html </b> DisplayProvider's images home.
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.3 $ $Date: 2005/05/23 17:44:55 $
 */
public final class ImagesHomeDisplayHelper implements DisplayHelper
{
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(ImagesHomeDisplayHelper.class);

    /**
     * Attribute key used in pageContext to store imagesHome dir. This usage
     * will be 100% changed in the future!
     * 
     * @TODO do not swallow this!
     */
    public static final String IMAGES_HOME_ATTRIBUTE_KEY = "VALUELIST_IMAGE_HOME_ATTRIBUTE_KEY";

    /**
     * @see org.webframe.web.page.web.util.DisplayHelper#help(javax.servlet.jsp.PageContext,
     *      java.lang.String)
     */
    public String help(PageContext pageContext, String key)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Replacing images home '"
                    + (String) pageContext
                            .getAttribute(IMAGES_HOME_ATTRIBUTE_KEY)
                    + "' in key '" + key + "'");
        }

        return key.replaceAll("@IMAGES_HOME@", (String) pageContext
                .getAttribute(IMAGES_HOME_ATTRIBUTE_KEY));
    }
}