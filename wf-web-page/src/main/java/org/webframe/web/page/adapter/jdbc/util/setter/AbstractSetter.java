
package org.webframe.web.page.adapter.jdbc.util.setter;

import org.webframe.web.page.adapter.jdbc.util.Setter;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2004/07/29 14:20:01 $
 */
public abstract class AbstractSetter implements Setter {

	/**
	 * @see org.webframe.web.page.adapter.jdbc.util.Setter#getReplacementString(java.lang.Object)
	 */
	public String getReplacementString(Object value) {
		return "?";
	}
}