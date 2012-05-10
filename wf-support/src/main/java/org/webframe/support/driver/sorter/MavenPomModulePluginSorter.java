package org.webframe.support.driver.sorter;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.webframe.support.driver.relation.ModulePluginDependency;
import org.webframe.support.driver.relation.ModulePluginDependencyUtil;
import org.webframe.support.util.SystemLogUtils;

/**
 * 根据jar包中的maven pom进行依赖深度排序
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-10 上午8:33:21
 * @version
 */
public class MavenPomModulePluginSorter extends AbstractModulePluginSorter {


	protected LinkedHashMap<String, JarURLConnection> sortByModulePluginDependency(Map<JarURLConnection, ModulePluginDependency> needSortedMap) {
		List<Entry<JarURLConnection, ModulePluginDependency>> mappingList = new ArrayList<Entry<JarURLConnection, ModulePluginDependency>>(needSortedMap.entrySet());
		Collections.sort(mappingList,
			new Comparator<Entry<JarURLConnection, ModulePluginDependency>>() {

				@Override
				public int compare(Entry<JarURLConnection, ModulePluginDependency> m1, Entry<JarURLConnection, ModulePluginDependency> m2) {
					return m1.getValue().compareTo(m2.getValue());
				}
			});
		LinkedHashMap<String, JarURLConnection> result = new LinkedHashMap<String, JarURLConnection>(16, 0.75f, true);
		for (Entry<JarURLConnection, ModulePluginDependency> entry : mappingList) {
			result.put(entry.getValue().getArtifactId(), entry.getKey());
			SystemLogUtils.secondPrintln("按照maven依赖关系排序："
						+ entry.getValue().key()
						+ "深度："
						+ entry.getValue().getIndex());
		}
		return result;
	}

	@Override
	protected LinkedHashMap<String, JarURLConnection> sorted(List<JarURLConnection> needSortedList)
				throws IOException {
		Map<JarURLConnection, ModulePluginDependency> analyzed = ModulePluginDependencyUtil.analyzeDependendy(
			needSortedList, new ArrayList<String>());
		return sortByModulePluginDependency(analyzed);
	}
}
