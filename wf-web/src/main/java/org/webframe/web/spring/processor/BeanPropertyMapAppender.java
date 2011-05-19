
package org.webframe.web.spring.processor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">huangguoqing</a>
 * @version $Id: BeanPropertyMapAppender.java,v 1.1.2.1 2010/04/23 07:57:54 huangguoqing Exp $
 *          Create: 2010-4-23 下午03:56:02
 */
public class BeanPropertyMapAppender extends AbstractBeanPropertyProcessor {

	private Map<String, Object>	appended;

	@SuppressWarnings("unchecked")
	protected Object getProcessedPropertyValue(Object originalValue) {
		Map<String, Object> newValue;
		if (originalValue == null) {
			newValue = new HashMap<String, Object>();
		} else {
			if (originalValue instanceof Map) {
				newValue = (Map<String, Object>) originalValue;
			} else {
				newValue = new HashMap<String, Object>();
				log.error(originalValue.getClass() + "不是Map类型！");
			}
		}
		newValue.putAll(getAppended());
		return newValue;
	}

	public Map<String, Object> getAppended() {
		return appended;
	}

	public void setAppended(Map<String, Object> appended) {
		this.appended = appended;
	}
}
