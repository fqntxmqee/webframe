/*
 * wf-core
 * Created on 2011-7-29-上午10:02:49
 */

package org.webframe.core.spring.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.webframe.core.util.ReflectionUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-29 上午10:02:49
 */
@SuppressWarnings("unchecked")
public class BeforeBeanInitializingProcessor implements BeanPostProcessor, Ordered {

	protected Log	log	= LogFactory.getLog(getClass());

	private int		order	= Ordered.LOWEST_PRECEDENCE;

	private String	beanName;

	private String	propertyName;

	private Object	appended;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals(getBeanName())) {
			if (getPropertyName() == null || "".equals(getPropertyName())) {
				throw new InvalidPropertyException(bean.getClass(), getPropertyName(), "指定属性不能为空或者null！");
			}
			beforeInitialization(bean);
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
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

	public Object getAppended() {
		return appended;
	}

	public void setAppended(Object appended) {
		this.appended = appended;
	}

	protected void beforeInitialization(Object bean) {
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);
		Class<?> propertyType = beanWrapper.getPropertyType(getPropertyName());
		if (Map.class.isAssignableFrom(propertyType)) {
			disposeMapProperty(beanWrapper);
		} else if (List.class.isAssignableFrom(propertyType)
					|| ReflectionUtils.getFieldValue(bean, getPropertyName()) instanceof List) {
			disposeListProperty(beanWrapper);
		} else if (propertyType.isArray()) {
			disposeArrayProperty(beanWrapper);
		} else {
			disposeProperty(beanWrapper);
		}
	}

	private void disposeMapProperty(BeanWrapperImpl beanWrapper) {
		Map<Object, Object> propertyValue;
		if (!beanWrapper.isReadableProperty(getPropertyName())) {
			Object bean = beanWrapper.getWrappedInstance();
			propertyValue = (Map<Object, Object>) ReflectionUtils.getFieldValue(bean, getPropertyName());
		} else {
			propertyValue = (Map<Object, Object>) beanWrapper.getPropertyValue(getPropertyName());
		}
		if (propertyValue == null) {
			propertyValue = new HashMap<Object, Object>();
		}
		Map<Object, Object> appendedMap = (Map<Object, Object>) appended;
		Set<Object> keys = appendedMap.keySet();
		for (Object key : keys) {
			if (propertyValue.containsKey(key)) {
				throw new InvalidPropertyException(beanWrapper.getWrappedClass(), getPropertyName(), "重复key---->"
							+ key);
			} else {
				propertyValue.put(key, appendedMap.get(key));
			}
		}
		beanWrapper.setPropertyValue(getPropertyName(), propertyValue);
	}

	private void disposeListProperty(BeanWrapperImpl beanWrapper) {
		List<Object> propertyValue;
		if (!beanWrapper.isReadableProperty(getPropertyName())) {
			Object bean = beanWrapper.getWrappedInstance();
			propertyValue = new ArrayList<Object>((List<Object>) ReflectionUtils.getFieldValue(bean, getPropertyName()));
		} else {
			propertyValue = (List<Object>) beanWrapper.getPropertyValue(getPropertyName());
		}
		if (propertyValue == null) {
			propertyValue = new ArrayList<Object>();
		}
		propertyValue.addAll((List<Object>) appended);
		beanWrapper.setPropertyValue(getPropertyName(), propertyValue);
	}

	private void disposeArrayProperty(BeanWrapperImpl beanWrapper) {
		Object[] propertyValue = null;
		if (!beanWrapper.isReadableProperty(getPropertyName())) {
			Object bean = beanWrapper.getWrappedInstance();
			propertyValue = (Object[]) ReflectionUtils.getFieldValue(bean, getPropertyName());
		} else {
			propertyValue = (Object[]) beanWrapper.getPropertyValue(getPropertyName());
		}
		if (propertyValue == null) {
			propertyValue = new Object[]{};
		}
		List<Object> list = new ArrayList<Object>(Arrays.asList(propertyValue));
		list.addAll((List<Object>) appended);
		beanWrapper.setPropertyValue(getPropertyName(), list.toArray());
	}

	private void disposeProperty(BeanWrapperImpl beanWrapper) {
		Object propertyValue = beanWrapper.convertForProperty(appended, getPropertyName());
		beanWrapper.setPropertyValue(getPropertyName(), propertyValue);
	}
}