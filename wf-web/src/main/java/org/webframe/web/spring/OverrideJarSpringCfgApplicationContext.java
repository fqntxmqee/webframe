/*
 * wf-web
 * Created on 2011-12-30-下午12:56:15
 */

package org.webframe.web.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.webframe.support.SpringContextUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * 支持覆盖jar包中的spring配置文件
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-30 下午12:56:15
 */
public class OverrideJarSpringCfgApplicationContext extends WFApplicationContext {

	@Override
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
		Resource[] resources = SpringContextUtils.getSpringContextResources();
		Map<String, Resource> resourceMap = new HashMap<String, Resource>();
		for (Resource resource : resources) {
			String key = resource.getFilename();
			if (resource instanceof FileSystemResource) {
				if (resourceMap.containsKey(key)) {
					SystemLogUtils.println("Override jar spring cfg: " + key);
				}
				resourceMap.put(key, resource);
			} else {
				int index = key.lastIndexOf("/");
				key = key.substring(index + 1);
				if (!resourceMap.containsKey(key)) {
					resourceMap.put(key, resource);
				} else {
					SystemLogUtils.println("Override jar spring cfg: " + key);
				}
			}
		}
		beanDefinitionReader.loadBeanDefinitions(resourceMap.values().toArray(new Resource[resourceMap.size()]));
	}
}
