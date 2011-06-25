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
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.6 $ $Date: 2006/03/29 19:47:49 $
 */
public interface PagingInfo
{
   /**
    * Returns an array of column (property) names.
    * 
    * @return a String array of column (property) names. null if no sorting
    *         information exists.
    */
   String getSortingColumn();

   /**
    * Returns an array of directions.
    * 
    * @return an Integer array of directions. null if no sorting information
    *         exists.
    */
   Integer getSortingDirection();

   /**
    * Getter for the curent page to display.
    * 
    * @return the curent page to display.
    */
   int getPagingPage();

   /**
    * Getter for the number of VOs per page. -1 if it is not set.
    * 
    * @return the number of VOs per page.
    */
   int getPagingNumberPer();
   
   /**
    * Getter for the total number VOs.
    * 
    * @return the total number of VOs.
    */
   int getTotalNumberOfEntries();

   /**
    * Getter for the total number VOs.
    * 
    * @return the total number of VOs.
    */
   int getTotalNumberOfPages();
   
   /**
    * @return The property used for focusing.
    */
   String getFocusProperty();
   
	 /**
     * @return The value for focusing.
     */
	String getFocusValue();

	/**
	 * Return true if the focusing is enabled.
     * @return boolean
     */
	boolean isFocusEnabled();
	 
	/**
     * @return true if is set DoFocus true
     */
	boolean isDoFocus();
	 
	/**
     * Used for generating links, if any errors found, doFocus is set to false
     * for next retrieving of the valueList.
     * @return boolean
     */
	boolean isDoFocusAgain();

	byte getFocusStatus();	
}