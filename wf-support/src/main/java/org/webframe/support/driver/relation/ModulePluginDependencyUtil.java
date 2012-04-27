
package org.webframe.support.driver.relation;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.webframe.support.driver.exception.ModulePluginException;
import org.webframe.support.util.ResourceUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-4-27 下午12:39:50
 * @version
 */
public abstract class ModulePluginDependencyUtil {

	public static final String				DEFAULT_MAVEN_POM	= "/META-INF/maven/**/pom.xml";

	public static final String				DEFAULT_GROUPID	= "${project.groupId}";

	public static final AntPathMatcher	pathMatcher			= new AntPathMatcher();

	public static List<JarURLConnection> sort(List<JarURLConnection> urls, List<String> patterns)
				throws IOException {
		return analyzeDependendy(urls, patterns);
	}

	public static List<JarURLConnection> analyzeDependendy(List<JarURLConnection> urls, List<String> patterns)
				throws IOException {
		List<Resource> resources = ResourceUtils.loadResources(urls,
			DEFAULT_MAVEN_POM);
		if (resources.size() != urls.size()) {
			throw new ModulePluginException("模块插件包中缺少pom.xml！");
		}
		Map<String, ModulePluginDependency> temp = new HashMap<String, ModulePluginDependency>();
		Map<String, ModulePluginDependency> allModulePluginDependency = new HashMap<String, ModulePluginDependency>();
		Map<JarURLConnection, ModulePluginDependency> needSort = new HashMap<JarURLConnection, ModulePluginDependency>();
		ModulePluginDependency first = null;
		SystemLogUtils.secondPrintln("根据查询到的pom对模块插件jar进行排序前的预处理！");
		for (int i = 0; i < resources.size(); i++) {
			Resource resource = resources.get(i);
			SystemLogUtils.thirdPrintln("解析Maven Pom：" + resource.toString());
			// 创建一个新的SAXReader解析器
			SAXReader saxReader = new SAXReader();
			try {
				// 通过输入源构造一个Document
				Document document = saxReader.read(resource.getInputStream());
				Element root = document.getRootElement();
				ModulePluginDependency dependency = findModulePluginDependency(root, patterns, true);
				if (dependency == null) {
					continue;
				}
				if (first == null) {
					first = dependency;
				}
				needSort.put(urls.get(i), dependency);
				temp.put(dependency.toString(), dependency);
				allModulePluginDependency.put(dependency.toString(), dependency);
				// 查找依赖
				Element dependenciseElement = root.element("dependencies");
				if (dependenciseElement != null) {
					List<?> dependencise = dependenciseElement.elements("dependency");
					for (Object object : dependencise) {
						ModulePluginDependency child = findModulePluginDependency((Element) object, patterns, false);
						if (child == null) {
							continue;
						}
						if (DEFAULT_GROUPID.equals(child.getGroupId())) {
							child.setGroupId(dependency.getGroupId());
						}
						child.increase();
						disposeMatchDependency(temp, child);
						dependency.putDependOn(child);
						allModulePluginDependency.put(child.toString(), child);
					}
				}
			} catch (DocumentException e) {
				SystemLogUtils.errorPrintln(e.getMessage());
			}
		}
		disposeFirstDependency(first, temp);
		SystemLogUtils.secondPrintln("对模块插件jar进行排序！");
		return sort(needSort);
	}

	private static List<JarURLConnection> sort(Map<JarURLConnection, ModulePluginDependency> needSort) {
		List<Entry<JarURLConnection, ModulePluginDependency>> mappingList = new ArrayList<Entry<JarURLConnection, ModulePluginDependency>>(needSort.entrySet());
		Collections.sort(mappingList,
			new Comparator<Entry<JarURLConnection, ModulePluginDependency>>() {

				@Override
				public int compare(Entry<JarURLConnection, ModulePluginDependency> m1, Entry<JarURLConnection, ModulePluginDependency> m2) {
					return m1.getValue().compareTo(m2.getValue());
				}
			});
		List<JarURLConnection> result = new ArrayList<JarURLConnection>();
		for (Entry<JarURLConnection, ModulePluginDependency> entry : mappingList) {
			result.add(entry.getKey());
			SystemLogUtils.secondPrintln("按照maven依赖关系排序："
						+ entry.getValue().toString()
						+ "深度："
						+ entry.getValue().getIndex());
		}
		return result;
	}

	private static void disposeFirstDependency(ModulePluginDependency first, Map<String, ModulePluginDependency> result) {
		if (first != null) {
			Map<String, ModulePluginDependency> childs = first.getDependOnMap();
			if (childs != null) {
				for (String key : childs.keySet()) {
					disposeMatchDependency(result, childs.get(key));
				}
			}
		}
	}

	private static void disposeMatchDependency(Map<String, ModulePluginDependency> result, ModulePluginDependency match) {
		if (result.containsKey(match.toString())) {
			ModulePluginDependency haven = result.get(match.toString());
			haven.increase(match.getIndex());
			match.setDependOnMap(haven.getDependOnMap());
		}
	}

	/**
	 * 从element元素中查询组件
	 * 
	 * @param root
	 * @param patterns
	 * @param findParent
	 * @return
	 * @author 黄国庆 2012-4-27 下午3:46:24
	 */
	private static ModulePluginDependency findModulePluginDependency(Element root, List<String> patterns, boolean findParent) {
		Node groupIdNode = root.element("groupId");
		if (groupIdNode == null && findParent) {
			Element parent = root.element("parent");
			if (parent != null) {
				groupIdNode = parent.element("groupId");
			}
		}
		if (groupIdNode == null) {
			return null;
		}
		String path = groupIdNode.getText();
		// 未匹配，继续循环
		if (!match(path, patterns)) {
			return null;
		}
		Node artifactIdNode = root.element("artifactId");
		if (artifactIdNode == null) {
			return null;
		}
		return new ModulePluginDependency(path, artifactIdNode.getText());
	}

	private static boolean match(String path, List<String> patterns) {
		if (patterns == null) {
			return true;
		}
		for (String pattern : patterns) {
			// 匹配规则
			if (pathMatcher.match(pattern, path)) {
				return true;
			}
		}
		return false;
	}
}
