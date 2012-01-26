
package org.webframe.test;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.webframe.support.SpringContextUtils;
import org.webframe.support.driver.resource.filter.JarResourceFilter;
import org.webframe.support.driver.resource.filter.ResourceFilter;

/**
 * 测试用例Spring Xml Context加载器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-11 下午04:24:14
 */
public class WFXmlContextLoader extends GenericXmlContextLoader {

	private BeanDefinitionReader	beanDefinitionReader	= null;

	protected BeanDefinitionReader createBeanDefinitionReader(final GenericApplicationContext context) {
		if (beanDefinitionReader == null) {
			beanDefinitionReader = super.createBeanDefinitionReader(context);
		}
		return beanDefinitionReader;
	}

	@Override
	protected void prepareContext(GenericApplicationContext context) {
		BeanDefinitionReader reader = createBeanDefinitionReader(context);
		ResourceFilter resourceFilter = getResourceFilter();
		if (resourceFilter == null) {
			resourceFilter = new JarResourceFilter();
		}
		reader.loadBeanDefinitions(resourceFilter.filter(SpringContextUtils.getSpringContextResources()));
	}

	protected ResourceFilter getResourceFilter() {
		return new JarResourceFilter();
	}
}
