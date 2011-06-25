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

import javax.servlet.jsp.JspException;

import org.webframe.web.page.web.tag.support.ColumnInfo;
import org.webframe.web.page.web.util.JspUtils;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.10 $ $Date: 2005/11/23 15:02:16 $
 */
public class DynaBeanColumnsTag extends DefaultColumnsTag
{

   private static final long serialVersionUID = -6875591300948358739L;

   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(DynaBeanColumnsTag.class);

   /**
    * @see javax.servlet.jsp.tagext.Tag#doEndTag()
    */
   public int doEndTag() throws JspException
   {
      ValueListSpaceTag rootTag = (ValueListSpaceTag) JspUtils.getParent(this, ValueListSpaceTag.class);

      DefaultRowTag rowTag = (DefaultRowTag) JspUtils.getParent(this, DefaultRowTag.class);
      DynaBean bean = (DynaBean) pageContext.getAttribute(rowTag.getBeanName());
      if (bean == null)
      {
         LOGGER.error("Zero results where returned.");
         return SKIP_BODY;
      }

      DynaClass dClass = bean.getDynaClass();

      StringBuffer sb = new StringBuffer();
      for (int i = 0, length = dClass.getDynaProperties().length; i < length; i++)
      {
         String name = dClass.getDynaProperties()[i].getName();
         if ((include.isEmpty() || include.contains(name)) && (exclude.isEmpty() || !exclude.contains(name)))
         {
            if (rowTag.getCurrentRowNumber() == 0)
            {
               String displayName = name.substring(0, 1).toUpperCase() + name.substring(1);
               rowTag.addColumnInfo(new ColumnInfo(displayName, name, defaultSort, null));
            }

            sb.append(rowTag.getDisplayProvider().getCellPreProcess(null));
            if (bean.get(name) == null)
            {
               sb.append(rootTag.getConfig().getNullToken());
            }
            else
            {
               sb.append(bean.get(name));
            }

            sb.append(rowTag.getDisplayProvider().getCellPostProcess());
         }

      }

      JspUtils.write(pageContext, sb.toString());

      release();

      return EVAL_PAGE;
   }
}