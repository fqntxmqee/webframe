
package org.webframe.web.spring;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;

/**
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">huangguoqing</a>
 * @version $Id: WFBeanWrapper.java,v 1.1.2.2 2010/05/25 01:36:56 huangguoqing Exp $ Create:
 *          2010-4-22 下午01:22:39
 */
public class WFBeanWrapper extends BeanWrapperImpl {

	public WFBeanWrapper() {
		this(true);
	}

	public WFBeanWrapper(boolean registerDefaultEditors) {
		super(registerDefaultEditors);
	}

	public WFBeanWrapper(Object object) {
		super(object);
	}

	public WFBeanWrapper(Class<?> clazz) {
		super(clazz);
	}

	public WFBeanWrapper(Object object, String nestedPath, Object rootObject) {
		super(object, nestedPath, rootObject);
	}

	public WFBeanWrapper(Object object, String nestedPath, BeanWrapperImpl superBw) {
		super(object, nestedPath, superBw);
	}

	@Override
	public void setPropertyValue(String propertyName, Object value) throws BeansException {
		super.setPropertyValue(propertyName, value);
	}

	@Override
	public void setPropertyValue(PropertyValue pv) throws BeansException {
		super.setPropertyValue(pv);
	}
}
