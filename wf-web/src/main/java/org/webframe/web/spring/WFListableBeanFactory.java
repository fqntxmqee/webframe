
package org.webframe.web.spring;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 扩展{@link org.springframework.beans.factory.support.DefaultListableBeanFactory}，使用自定义
 * {@link WFBeanWrapper}，更改Bean实例化过程
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:24:41
 * @version
 */
public class WFListableBeanFactory extends DefaultListableBeanFactory {

	public WFListableBeanFactory() {
		super();
	}

	public WFListableBeanFactory(BeanFactory parentBeanFactory) {
		super(parentBeanFactory);
	}

	@Override
	protected BeanWrapper instantiateBean(String beanName, RootBeanDefinition mbd) {
		try {
			Object beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, this);
			BeanWrapper bw = new WFBeanWrapper(beanInstance);
			initBeanWrapper(bw);
			return bw;
		} catch (Throwable ex) {
			throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
		}
	}

	@Override
	public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		try {
			return super.getBeanDefinition(beanName);
		} catch (NoSuchBeanDefinitionException ne) {
			BeanFactory beanFactory = getParentBeanFactory();
			if (beanFactory instanceof WFListableBeanFactory) {
				return ((WFListableBeanFactory) beanFactory).getBeanDefinition(beanName);
			} else {
				throw ne;
			}
		}
	}
}
