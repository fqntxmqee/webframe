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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListHandler;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.web.ValueListRequestUtil;
import org.webframe.web.page.web.util.JspUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager

/** This tag is used if the pull pattern is used.
 * 
 * @todo Document this tag.
 * 
 * @author Matthew L. Wilson, A.Zachar
 * @version $Revision: 1.18 $ $Date: 2005/11/23 14:51:53 $
 */
public class ValueListRetriever extends ConfigurableTag
{
   private static final long serialVersionUID = 8975676126886164801L;

   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(ValueListRetriever.class);

   private String valueListName = null;

   private String focusProperty = null;

   private String focusValue = null;

   /**
    * @see javax.servlet.jsp.tagext.Tag#doStartTag()
    */
   public int doStartTag() throws JspException
   {
      getCellAttributes().getMap().clear();
      return super.doStartTag();
   }

   /**
    * @see javax.servlet.jsp.tagext.Tag#doEndTag()
    */
   public int doEndTag() throws JspException
   {
      ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);

      ValueListInfo info = ValueListRequestUtil.buildValueListInfo((HttpServletRequest) pageContext.getRequest(), rootTag.getTableInfo()
            .getId());
      info.getFilters().putAll(getCellAttributes().getMap());

      WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());

      if (context == null)
      {
         final String message = "Cannot locate the spring WebApplicationContext. Is it configured in your web.xml?";
         LOGGER.error(message);
         throw new JspException(message);
      }

      ValueListHandler vlh = (ValueListHandler) context.getBean(ValueListHandler.DEFAULT_SERVICE_NAME, ValueListHandler.class);
      if (vlh == null)
      {
         final String message = "Cannot locate the ValueListHanlder in the WebApplicationContext, under the name: \""
               + ValueListHandler.DEFAULT_SERVICE_NAME + "\"";
         LOGGER.error(message);
         throw new JspException(message);
      }

      info.setFocusValue(focusValue);
      info.setFocusProperty(focusProperty);

      if (LOGGER.isDebugEnabled())
      {
         LOGGER.debug("Focus settings was setted up. Focus property '" + focusProperty + "', focus value '" + focusValue + "'");
      }

      ValueList list = vlh.getValueList(valueListName, info);
      pageContext.getRequest().setAttribute(rootTag.getTableInfo().getName(), list);
      rootTag.setValueList(list);

      if (LOGGER.isDebugEnabled())
      {
         LOGGER.debug("ValueList was retrieved from adapter inside of the jsp.");
      }

      release();

      return EVAL_PAGE;
   }

   /**
    * @param valueListName
    *           The valueListName to set.
    */
   public void setName(String valueListName)
   {
      this.valueListName = valueListName;
   }

   /**
    * @return Returns the valueListName.
    */
   public String getName()
   {
      return valueListName;
   }

   /**
    * @param focusProperty
    *            The focusProperty set usually name of column used to be id in
    *            table.
    */
   public void setFocusProperty(String focusProperty)
   {
      this.focusProperty = focusProperty;
   }

   /**
    * @param focusValue
    *            The focusValue set value on which will be occured in the
    *            generated table.
    */
   public void setFocusValue(String focusValue)
   {
      this.focusValue = focusValue;
   }

   private void reset()
   {
      this.focusProperty = null;
      this.focusValue = null;
      this.valueListName = null;
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