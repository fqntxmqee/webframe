/*
 * com.berheley.bi.basic
 * Created on 2011-12-31-上午08:25:32
 */

package org.webframe.support.driver.resource.filter;

import java.util.Iterator;
import java.util.List;

import org.springframework.core.io.Resource;

/**
 * Resource集合过滤器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 上午08:25:32
 */
public interface ResourceFilter {

	/**
	 * 过滤Resource集合
	 * 
	 * @param resources
	 * @return 过滤后的结果集
	 * @author 黄国庆 2011-12-31 上午08:35:58
	 */
	Resource[] filter(Resource[] resources);

	/**
	 * 过滤Resource集合
	 * 
	 * @param resources
	 * @return 过滤后的结果集
	 * @author 黄国庆 2011-12-31 上午08:35:58
	 */
	Resource[] filter(List<Resource> resources);

	/**
	 * 过滤Resource集合
	 * 
	 * @param resources
	 * @return 过滤后的结果集
	 * @author 黄国庆 2011-12-31 上午08:31:04
	 */
	Resource[] filter(Iterator<Resource> resources);
}
