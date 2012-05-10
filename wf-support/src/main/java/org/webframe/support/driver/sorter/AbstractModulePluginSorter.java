package org.webframe.support.driver.sorter;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.webframe.support.driver.resource.jar.JarResourcePatternResolver;


/**
 * 提供公共排序的功能
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-10 上午8:32:04
 * @version
 */
public abstract class AbstractModulePluginSorter implements ModulePluginSorter {

	private List<String>	sortedKeys	= new ArrayList<String>();

	@Override
	public Collection<JarURLConnection> sort(List<JarURLConnection> needSortedList)
				throws IOException {
		LinkedHashMap<String, JarURLConnection> map = sorted(needSortedList);
		proccessSortedKeys(map.keySet());
		JarResourcePatternResolver.sortResolver(getSortedKeys());
		return map.values();
	}

	/**
	 * 返回一个有排序结构的Map集合
	 * 
	 * @param needSortedList
	 * @return
	 * @throws IOException
	 * @author 黄国庆 2012-5-10 下午3:01:36
	 */
	protected abstract LinkedHashMap<String, JarURLConnection> sorted(List<JarURLConnection> needSortedList)
				throws IOException;

	/**
	 * 处理排序集合
	 * 
	 * @param sortedKeys
	 * @author 黄国庆 2012-5-10 下午3:01:06
	 */
	protected void proccessSortedKeys(Set<String> sortedKeys) {
		for (String sortedKey : sortedKeys) {
			this.sortedKeys.add(sortedKey);
		}
	}

	public List<String> getSortedKeys() {
		return Collections.unmodifiableList(sortedKeys);
	}
}
