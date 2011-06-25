
package org.webframe.web.page.adapter.jdbc.util.setter;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.5 $ $Date: 2005/08/19 16:06:29 $
 */
public abstract class AbstractArraySetter extends AbstractSetter {

	private boolean	useBindVarables	= true;

	/**
	 * @see org.webframe.web.page.adapter.jdbc.util.Setter#getReplacementString(java.lang.Object)
	 */
	public String getReplacementString(Object value) {
		if (value instanceof String) {
			return (useBindVarables) ? "?" : (String) value;
		} else if (value instanceof Object[]) {
			Object[] values = (Object[]) value;
			StringBuffer sb = new StringBuffer();
			if (useBindVarables) {
				for (int i = 0, length = values.length; i < length; i++) {
					sb.append("?");
					if ((i + 1) < length) {
						sb.append(",");
					}
				}
			} else {
				for (int i = 0, length = values.length; i < length; i++) {
					sb.append(values[i]);
					if ((i + 1) < length) {
						sb.append(",");
					}
				}
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the useBindVarables.
	 */
	public boolean isUseBindVarables() {
		return useBindVarables;
	}

	/**
	 * @param useBindVarables The useBindVarables to set.
	 */
	public void setUseBindVarables(boolean useBindVarables) {
		this.useBindVarables = useBindVarables;
	}
}