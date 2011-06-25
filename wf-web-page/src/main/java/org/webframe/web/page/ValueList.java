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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A <code>ValueList</code> contains a <code>List</code> of beans and a <code>ValueListInfo</code>
 * object which defines the sorting and paging of the contained list.
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2004/06/25 16:42:41 $
 */
public interface ValueList extends Serializable, Iterator<Object> {

	/**
	 * Returns the embeded <code>List</code>
	 * 
	 * @return The embeded <code>List</code>
	 */
	List<Object> getList();

	/**
	 * Returns the embeded <code>ValueListInfo</code>
	 * 
	 * @return The embeded <code>ValueListInfo</code>
	 */
	ValueListInfo getValueListInfo();

	/**
	 * Returns if there more Objects in the Iterator
	 * 
	 * @return true if there more Objects in the Iterator, other wise false
	 */
	boolean hasNext();

	/**
	 * Gets the next Object in the Iterator.
	 * 
	 * @throws NoSuchElementException If element does not exist.
	 * @return The next Object in the Iterator.
	 */
	Object next() throws NoSuchElementException;
}