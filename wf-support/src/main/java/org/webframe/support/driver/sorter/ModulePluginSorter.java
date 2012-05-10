
package org.webframe.support.driver.sorter;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Collection;
import java.util.List;

/**
 * 模块插件驱动排序接口，先排序后注册
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-10 上午8:31:22
 * @version
 */
public interface ModulePluginSorter {

	/**
	 * 排序后的集合
	 * 
	 * @param needSortedList 未排序集合
	 * @return 具有排序功能集合
	 * @throws IOException
	 * @author 黄国庆 2012-5-10 下午1:30:14
	 */
	Collection<JarURLConnection> sort(List<JarURLConnection> needSortedList)
				throws IOException;
}
