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

package org.webframe.web.page.web.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A lot of this functionality is in the Struts ResponceUtils, but I do not want to create a
 * dependicy to struts.
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.7 $ $Date: 2005/11/23 14:32:58 $
 */
public final class JspUtils {

	private static final Log	LOGGER	= LogFactory.getLog(JspUtils.class);

	/** Private to protect singleton. */
	private JspUtils() {
	}

	/**
	 * Write the specified text as the response to the writer associated with this page.
	 * <strong>WARNING </strong>- If you are writing body content from the <code>doAfterBody()</code>
	 * method of a custom tag class that implements <code>BodyTag</code>, you should be calling
	 * <code>writePrevious()</code> instead.
	 * 
	 * @param pageContext The PageContext object for this page
	 * @param text The text to be written
	 * @exception JspException if an input/output error occurs
	 */
	public static void write(PageContext pageContext, String text) throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(text);
		} catch (IOException e) {
			LOGGER.error("JspUtils.write() exception...", e);
			throw new JspException(e);
		}
	}

	/**
	 * Write the specified text as the response to the writer associated with the body content for
	 * the tag within which we are currently nested.
	 * 
	 * @param pageContext The PageContext object for this page
	 * @param text The text to be written
	 * @exception JspException if an input/output error occurs
	 */
	public static void writePrevious(PageContext pageContext, String text) throws JspException {
		JspWriter writer = pageContext.getOut();
		if (writer instanceof BodyContent) {
			writer = ((BodyContent) writer).getEnclosingWriter();
		}
		try {
			writer.print(text);
		} catch (IOException e) {
			LOGGER.error("JspUtils.writePrevious() exception...", e);
			throw new JspException(e);
		}
	}

	/**
	 * Converts a stirng into a collection of strings.
	 * 
	 * @param value The string to be parsed.
	 * @param token The token to be used.
	 * @return A Collection of String(s).
	 */
	public static Collection<String> toCollection(String value, String token) {
		if (value == null || value.length() == 0) {
			return Collections.emptyList();
		}
		Collection<String> elements = new ArrayList<String>();
		for (StringTokenizer st = new StringTokenizer(value, token); st.hasMoreElements();) {
			elements.add(st.nextToken());
		}
		return elements;
	}

	public static String format(Object value, String format, Locale loc) {
		if (value == null) {
			return "";
		} else {
			if (value instanceof Number) {
				if (format == null || format.length() == 0) {
					return value.toString();
				}
				MessageFormat mf = new MessageFormat("{0,number," + format + "}");
				if (loc != null) {
					mf.setLocale(loc);
					mf.applyPattern(mf.toPattern());
				}
				return mf.format(new Object[]{
					value});
			} else if (value instanceof java.util.Date) {
				if (format == null || format.length() == 0) {
					// TODO: get the default date format in here somehow. format =
					// SystemProperties.getProperty("default.dateFormat", "EEE, MMM
					// d, ''yy");
					format = "EEE, MMM d, ''yy";
				}
				MessageFormat mf = new MessageFormat("{0,date," + format + "}");
				if (loc != null) {
					mf.setLocale(loc);
					mf.applyPattern(mf.toPattern());
				}
				return mf.format(new Object[]{
					value});
			} else if (value instanceof Calendar) {
				if (format == null || format.length() == 0) {
					// TODO: get the default date format in here somehow. format =
					// SystemProperties.getProperty("default.dateFormat", "EEE, MMM
					// d, ''yy");
					format = "EEE, MMM d, ''yy";
				}
				MessageFormat mf = new MessageFormat("{0,date," + format + "}");
				if (loc != null) {
					mf.setLocale(loc);
					mf.applyPattern(mf.toPattern());
				}
				return mf.format(new Object[]{
					value});
			} else {
				return value.toString();
			}
		}
	}

	public static String toAttributesString(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			sb.append(key).append("=\"").append(map.get(key)).append("\" ");
		}
		return sb.toString();
	}

	public static Tag getParent(Tag self, Class<?> klass) throws JspException {
		Tag tag = self.getParent();
		while (!(klass.isAssignableFrom(tag.getClass()))) {
			tag = tag.getParent();
			if (tag == null) {
				final String message = "Parent tag of class " + klass + " of the tag's class " + self + " was not found.";
				LOGGER.error(message);
				throw new JspException(message);
			}
		}
		return tag;
	}
}