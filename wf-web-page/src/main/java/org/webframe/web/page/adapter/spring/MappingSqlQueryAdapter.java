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

package org.webframe.web.page.adapter.spring;

import java.util.List;

import org.springframework.jdbc.object.MappingSqlQuery;
import org.webframe.web.page.DefaultListBackedValueList;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.ValueListInfo;
import org.webframe.web.page.adapter.AbstractValueListAdapter;

/**
 * This adapter handles the standard functionality of creating a query and execution it...
 * org.webframe.web.page.adapter.spring.MappingSqlQueryAdapter
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.1 $ $Date: 2004/11/10 13:50:25 $
 */
public class MappingSqlQueryAdapter extends AbstractValueListAdapter {

	private MappingSqlQuery<Object>	query;

	/* (non-Javadoc)
	 * @see org.webframe.web.page.ValueListAdapter#getValueList(java.lang.String, org.webframe.web.page.ValueListInfo)
	 */
	public ValueList getValueList(String name, ValueListInfo info) {
		List<Object> list = query.execute();
		return new DefaultListBackedValueList(list, info);
	}
}