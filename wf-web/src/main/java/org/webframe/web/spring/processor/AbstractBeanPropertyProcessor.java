
package org.webframe.web.spring.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">huangguoqing</a>
 * @version $Id: AbstractBeanPropertyProcessor.java,v 1.1.2.1 2010/04/23 07:57:54 huangguoqing Exp $
 *          Create: 2010-4-23 下午03:56:17
 */
public abstract class AbstractBeanPropertyProcessor implements BeanFactoryPostProcessor, Ordered {

	protected Log	log	= LogFactory.getLog(getClass());

	private int		order	= Ordered.LOWEST_PRECEDENCE;

	private String	beanName;

	private String	propertyName;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition(getBeanName());
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		Object value = null;
		PropertyValue propertyValue = propertyValues.getPropertyValue(getPropertyName());
		if (propertyValue != null) {
			value = propertyValue.getValue();
		}
		if (log.isDebugEnabled()) {
			log.debug("Original value of " + getBeanName() + "." + getPropertyName() + ": " + value);
		}
		Object appendedValue = getProcessedPropertyValue(value);
		if (log.isDebugEnabled()) {
			log.debug("New value of " + getBeanName() + "." + getPropertyName() + ": " + appendedValue);
		}
		propertyValues.addPropertyValue(getPropertyName(), appendedValue);
	}

	/**
	 * 根据元素数据，获取处理后的数据
	 * 
	 * @param originalValue
	 * @return
	 * @author: 黄国庆 2011-1-20 下午04:50:13
	 */
	protected abstract Object getProcessedPropertyValue(Object originalValue);

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}