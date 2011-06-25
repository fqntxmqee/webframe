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

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.webframe.web.page.web.tag.support.ParamAddable;
import org.webframe.web.page.web.tag.support.Spacer;
import org.webframe.web.page.web.util.JspUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The tag that append parameters to url for any paramaddable tag in table,such
 * are tag for root and tag for action.
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.11 $ $Date: 2005/11/23 14:51:53 $
 */
public class AddParamTag extends BodyTagSupport
{

   private static final long serialVersionUID = 6118198463511925234L;

   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(AddParamTag.class);

   /** The name of the parameter used in rendered URL. */
   private String name = null;

   /**
    * The name of the dynamic property, which values are used like the
    * attribute value, but dynamicaly retrevied for the each row.
    */
   private String property = null;

   /**
    * The static value of the attribute name used in rendered URL like
    * url?(ACTION_TEMP_PARAM_PREFIX)name=value .
    */
   private String value = null;

   /**
    * if true, append prefix ACTION_TEMP_PARAM_PREFIX before every action
    * parameter.
    */
   private boolean temp = false;

   /**
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    * @see javax.servlet.jsp.tagext.Tag#doStartTag()
    */
   public int doStartTag() throws JspException
   {
      ParamAddable parent = (ParamAddable) JspUtils.getParent(this, ParamAddable.class);

      if ((property != null) && (value != null))
      {
         final String message = "For one parameter" + name
               + "you can not use two values (first dynamic from the property, 2nd static from the value";
         LOGGER.error(message);
         throw new JspException(message);
      }

      if (name == null)
      {
         // use the same name as the name of property, if name is not
         // specified
         name = property;
      }

      if (property != null)
      {
         DefaultRowTag rowTag = (DefaultRowTag) JspUtils.getParent(this, DefaultRowTag.class);
         Object bean = pageContext.getAttribute(rowTag.getBeanName());
         if (bean != null && !(bean instanceof Spacer))
         {
            try
            {
               value = String.valueOf(PropertyUtils.getProperty(bean, property));
            }
            catch (Exception e)
            {
               LOGGER.error("<vlh:addParam> Error getting property '" + property + "'.");
               value = null;
            }
         }
         else
         {
            if (LOGGER.isDebugEnabled())
            {
               LOGGER.debug("Row's JavaBean '" + rowTag.getBeanName() + "' is null or Valuelist is empty!");
            }
            value = null;
         }
      }

      if (value == null)
      {
         if (LOGGER.isInfoEnabled())
         {
            LOGGER.info("The param '" + addActionParamPrefix(name) + "' is skiped, because it's value is null!");
         }
      }
      else
      {

         parent.addParam(addActionParamPrefix(name), value);
         if (LOGGER.isDebugEnabled())
         {
            LOGGER.debug("The param '" + addActionParamPrefix(name) + "' was added with the value '" + value + "'.");
         }
      }

      release();

      return SKIP_BODY;
   }

   /**
    * @param param
    *            Add action param prefix if it is a temp parameter.
    */
   protected String addActionParamPrefix(String param)
   {
      if (isTemp())
      {
         return ActionTag.ACTION_TEMP_PARAM_PREFIX + param;
      }
      else
      {
         return param;
      }
   }

   /**
    * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
    */
   public int doAfterBody() throws JspException
   {
      bodyContent.clearBody();
      return SKIP_BODY;
   }

   /**
    * @see javax.servlet.jsp.tagext.Tag#doEndTag()
    */
   public int doEndTag() throws JspException
   {
      int result = super.doEndTag();
      reset();
      return result;
   }

   /**
    * @return Returns the property.
    */
   public String getProperty()
   {
      return this.property;
   }

   /**
    * @param property
    *            The property to set.
    */
   public void setProperty(String property)
   {
      this.property = property;
   }

   /**
    * @return Returns the name.
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * @param name
    *            The url parameter name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return Returns the value.
    */
   public String getValue()
   {
      return this.value;
   }

   /**
    * @param value The value to set.
    */
   public void setValue(String value)
   {
      this.value = value;
   }

   /**
    * @return Returns the temp.
    */
   public boolean isTemp()
   {
      return temp;
   }

   /**
    * Default is false. If true,
    * append prefix ACTION_TEMP_PARAM_PREFIX before every action  parameter.
    * @param temp The temp to set.
    */
   public void setTemp(boolean temp)
   {
      this.temp = temp;
   }

   private void reset()
   {
      this.name = null;
      this.property = null;
      this.temp = false;
      this.value = null;
   }

   /**
    * Called on a Tag handler to release state.
    * The page compiler guarantees that JSP page implementation
    * objects will invoke this method on all tag handlers,
    * but there may be multiple invocations on doStartTag and doEndTag in between.
    * 
    * @see javax.servlet.jsp.tagext.Tag#release()
    */
   public void release()
   {
      super.release();
      reset();
   }
}