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

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/** 
 * @author Matthew L. Wilson 
 * @version $Revision: 1.3 $ $Date: 2005/11/23 14:45:41 $
 */
public class DefaultRowTei extends TagExtraInfo
{

   /** @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
    */
   public VariableInfo[] getVariableInfo(TagData data)
   {
      String beanType = data.getAttributeString("type");

      // current row
      VariableInfo lInfo1 = new VariableInfo(data.getAttributeString("bean"), beanType == null ? "java.lang.Object" : beanType, true,
            VariableInfo.NESTED);

      // current row number
      VariableInfo lInfo2 = new VariableInfo(data.getAttributeString("bean") + "RowNumber", "java.lang.Integer", true, VariableInfo.NESTED);

      return new VariableInfo[]
      { lInfo1, lInfo2 };
   }

}