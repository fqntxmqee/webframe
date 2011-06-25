/*
 * Created on 27.1.2005
 * azachar
 */

package org.webframe.web.page.web.tag.support;

import org.webframe.web.page.DefaultListBackedValueList;

/**
 * The singleton ValueListNullSpacer is used to render an empty valuelist, ussally you get this,
 * when any error occured in service's handler.
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.2 $ $Date: 2005/08/19 16:06:30 $
 */
final public class ValueListNullSpacer extends DefaultListBackedValueList {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 5824767749744562044L;

	private static ValueListNullSpacer	valueListSpacer;

	public static ValueListNullSpacer getInstance() {
		if (valueListSpacer == null) {
			valueListSpacer = new ValueListNullSpacer();
		}
		return valueListSpacer;
	}

	private ValueListNullSpacer() {
		super();
		getList().add(new Spacer());
	}
}
