/*
 * wf-support
 * Created on 2011-12-31-上午09:26:27
 */

package org.webframe.support.driver.resource.filter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.webframe.support.util.SystemLogUtils;

/**
 * 过滤jar包中重复的spring cfg文件
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 上午09:26:27
 */
public class JarResourceFilter extends AbstractResourceFilter {

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.resource.filter.ResourceFilter#filter(java.util.Iterator)
	 */
	@Override
	public Resource[] filter(Iterator<Resource> resources) {
		Map<String, Resource> resourceMap = new LinkedHashMap<String, Resource>();
		while (resources.hasNext()) {
			Resource resource = resources.next();
			String key = resource.getFilename();
			if (resource instanceof FileSystemResource) {
				if (resourceMap.containsKey(key)) {
					SystemLogUtils.println("Filter jar spring file: " + key);
				}
				resourceMap.put(key, resource);
			} else {
				int index = key.lastIndexOf("/");
				key = key.substring(index + 1);
				if (!resourceMap.containsKey(key)) {
					resourceMap.put(key, resource);
				} else {
					SystemLogUtils.println("Filter jar spring file: " + key);
				}
			}
		}
		return resourceMap.values().toArray(new Resource[resourceMap.size()]);
	}
}
