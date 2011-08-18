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

package org.webframe.web.page;

/**
 * This class abstracts the means by which a ValueList is retrieved by the service.
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.5 $ $Date: 2005/04/07 14:07:40 $
 */
public interface ValueListAdapter {

	/** Constant to tell the Handler to do nothing. */
	static final int	DO_NOTHING	= 0;

	/** Constant to tell the Handler to do sorting. */
	static final int	DO_SORT		= 1;

	/** Constant to tell the Handler to do paging. */
	static final int	DO_PAGE		= 2;

	/** Constant to tell the Handler to do filtering. */
	static final int	DO_FILTER	= 4;

	/** Constant to tell the Handler to do focusing. */
	static final int	DO_FOCUS		= 8;

	/**
	 * Gets a ValueList
	 * 
	 * @param info The <CODE>ValueList</CODE> information
	 * @param name The name of the <CODE>ValueList</CODE>
	 * @return The <CODE>ValueList</CODE>
	 */
	<X> ValueList<X> getValueList(String name, ValueListInfo info);

	/**
	 * This method tells the Service what still needs to be done on the Collection before returning
	 * the data.
	 * 
	 * @return A bitwise or combination of DO_NOTHING, DO_SORT, DO_PAGE and DO_FILTER. For
	 *         example:<br> <pre> public int getAdapterType() { return DO_SORT | DO_PAGE; } </pre>
	 */
	int getAdapterType();
}
