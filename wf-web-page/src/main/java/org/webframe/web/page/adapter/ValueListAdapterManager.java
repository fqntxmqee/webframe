
package org.webframe.web.page.adapter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapperImpl;
import org.webframe.core.spring.processor.BeforeBeanInitializingProcessor;
import org.webframe.web.page.ValueListAdapter;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-21
 *          上午10:10:16
 */
@SuppressWarnings("unchecked")
public class ValueListAdapterManager extends BeforeBeanInitializingProcessor {

	private static final Map<String, ValueListAdapter>	adapters								= new HashMap<String, ValueListAdapter>(16);

	private static final String								valueListHandlerBeanName		= "valueListHandler";

	private static final String								valueListHandlerPropertyName	= "config.adapters";

	@Override
	public String getBeanName() {
		return super.getBeanName() == null ? valueListHandlerBeanName : super.getBeanName();
	}

	@Override
	public String getPropertyName() {
		return super.getPropertyName() == null ? valueListHandlerPropertyName : super.getPropertyName();
	}

	@Override
	protected void beforeInitialization(Object bean) {
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);
		Map<String, ValueListAdapter> propertyValue = (Map<String, ValueListAdapter>) beanWrapper.getPropertyValue(getPropertyName());
		if (propertyValue != null) adapters.putAll(propertyValue);
		super.beforeInitialization(bean);
	}

	public static Map<String, ValueListAdapter> getAllAdapters() {
		return adapters;
	}
}
