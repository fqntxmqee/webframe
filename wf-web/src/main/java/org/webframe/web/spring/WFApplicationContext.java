
package org.webframe.web.spring;

import java.util.Arrays;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.webframe.support.SpringContextUtils;
import org.webframe.support.driver.resource.filter.JarResourceFilter;
import org.webframe.support.driver.resource.filter.ResourceFilter;
import org.webframe.support.util.ResourceUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * 扩展spring XmlWebApplicationContext；添加自定义spring cfg获取，并支持ResourceFilter过滤
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">huangguoqing</a>
 * @version $Id: WFApplicationContext.java,v 1.1.2.2 2010/08/06 08:46:31 huangguoqing Exp $ Create:
 *          2010-4-22 下午01:18:10
 */
public class WFApplicationContext extends XmlWebApplicationContext {

	private ResourceFilter	resourceFilter	= null;

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
		ResourceFilter resourceFilter = getResourceFilter();
		if (resourceFilter == null) {
			resourceFilter = new JarResourceFilter();
		}
		Resource[] resources = resourceFilter.filter(SpringContextUtils.getSpringContextResources());
		SystemLogUtils.rootPrintln("加载BeanDefinitions开始!");
		for (Resource resource : resources) {
			beanDefinitionReader.loadBeanDefinitions(resource);
			SystemLogUtils.secondPrintln("spring配置文件：" + ResourceUtils.getShotFileName(resource));
		}
		SystemLogUtils.rootPrintln("加载BeanDefinitions结束!");
	}

	@Override
	public String[] getConfigLocations() {
		String[] locations = super.getConfigLocations();
		if (locations.length == 1 && Arrays.asList(locations).contains(DEFAULT_CONFIG_LOCATION)) {
			return new String[0];
		}
		return locations;
	}

	protected ResourceFilter getResourceFilter() {
		if (resourceFilter == null) {
			resourceFilter = new JarResourceFilter();
		}
		return resourceFilter;
	}

	public void setResourceFilter(ResourceFilter resourceFilter) {
		this.resourceFilter = resourceFilter;
	}
}
