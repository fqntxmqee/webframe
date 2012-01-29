
package org.webframe.web.spring;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;

/**
 * 扩展{@link org.springframework.beans.BeanWrapperImpl}，提供spring配置文件中Bean实例化之后，对对象相关属性的处理
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:20:19
 * @version
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
