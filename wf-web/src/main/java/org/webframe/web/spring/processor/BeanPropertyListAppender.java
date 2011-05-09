
package org.webframe.web.spring.processor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-21 下午02:14:17
 */
public class BeanPropertyListAppender extends AbstractBeanPropertyProcessor {

	private List<Object>	appended;

	/* (non-Javadoc)
	 * @see com.berheley.wf.extend.spring.AbstractBeanPropertyProcessor#getProcessedPropertyValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object getProcessedPropertyValue(Object originalValue) {
		List<Object> newValue;
		if (originalValue == null) {
			newValue = new LinkedList<Object>();
		} else {
			if (originalValue instanceof List) {
				newValue = (List<Object>) originalValue;
			} else {
				newValue = new LinkedList<Object>();
				log.error(originalValue.getClass() + "不是List类型！");
			}
		}
		newValue.addAll(getAppended());
		return newValue;
	}

	public List<Object> getAppended() {
		if (this.appended == null) {
			this.setAppended(new LinkedList<Object>());
		}
		return appended;
	}

	public void setAppended(List<Object> appended) {
		this.appended = appended;
	}
}
