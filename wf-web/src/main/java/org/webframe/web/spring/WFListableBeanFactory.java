
package org.webframe.web.spring;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 类功能描述：
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">huangguoqing</a>
 * @version $Id: BerheleyListableBeanFactory.java,v 1.1.2.1 2010/04/22 07:46:09 huangguoqing Exp $
 *          Create: 2010-4-22 下午01:19:43
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
