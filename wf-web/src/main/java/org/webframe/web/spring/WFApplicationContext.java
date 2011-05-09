
package org.webframe.web.spring;

import java.util.Arrays;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.webframe.support.SpringContextUtils;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">huangguoqing</a>
 * @version $Id: BerheleyWebApplicationContext.java,v 1.1.2.2 2010/08/06 08:46:31 huangguoqing Exp $
 *          Create: 2010-4-22 下午01:18:10
 */
public class WFApplicationContext extends XmlWebApplicationContext {

	public WFApplicationContext() {
		super();
		ServiceHelper.init(this);
	}

	@Override
	protected DefaultListableBeanFactory createBeanFactory() {
		return new WFListableBeanFactory(getInternalParentBeanFactory());
	}

	@Override
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
		beanDefinitionReader.loadBeanDefinitions(SpringContextUtils.getSpringContextResources());
	}

	@Override
	public String[] getConfigLocations() {
		String[] locations = super.getConfigLocations();
		if (locations.length == 1 && Arrays.asList(locations).contains(DEFAULT_CONFIG_LOCATION)) {
			return new String[0];
		}
		return locations;
	}
}
