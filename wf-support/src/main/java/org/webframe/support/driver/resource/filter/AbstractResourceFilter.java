/*
 * wf-support
 * Created on 2011-12-31-上午09:23:53
 */

package org.webframe.support.driver.resource.filter;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;

/**
 * Spring Resource 过滤器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 上午09:23:53
 */
public abstract class AbstractResourceFilter implements ResourceFilter {

	@Override
	public Resource[] filter(Resource[] resources) {
		return filter(Arrays.asList(resources));
	}

	@Override
	public Resource[] filter(List<Resource> resources) {
		return filter(resources.iterator());
	}
}
