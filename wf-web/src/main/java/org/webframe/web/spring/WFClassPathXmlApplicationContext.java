
package org.webframe.web.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-2 上午11:33:16
 */
public class WFClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

	public WFClassPathXmlApplicationContext() {
	}

	public WFClassPathXmlApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	public WFClassPathXmlApplicationContext(String configLocation) throws BeansException {
		super(configLocation);
	}

	public WFClassPathXmlApplicationContext(String... configLocations) throws BeansException {
		super(configLocations);
	}

	public WFClassPathXmlApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
		super(configLocations, parent);
	}

	public WFClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
		super(configLocations, refresh);
	}

	public WFClassPathXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
				throws BeansException {
		super(configLocations, refresh, parent);
	}

	public WFClassPathXmlApplicationContext(String path, Class<?> clazz) throws BeansException {
		super(path, clazz);
	}

	public WFClassPathXmlApplicationContext(String[] paths, Class<?> clazz) throws BeansException {
		super(paths, clazz);
	}

	public WFClassPathXmlApplicationContext(String[] paths, Class<?> clazz, ApplicationContext parent)
				throws BeansException {
		super(paths, clazz, parent);
	}

	@Override
	protected DefaultListableBeanFactory createBeanFactory() {
		return new WFListableBeanFactory(getInternalParentBeanFactory());
	}
}
