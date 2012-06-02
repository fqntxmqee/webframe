
package org.webframe.support.driver.relation;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * ModulePluginDependency解析工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-4-27 下午12:39:50
 * @version
 */
public abstract class ModulePluginDependencyUtil {

	public static final String				DEFAULT_MAVEN_POM	= "/META-INF/maven/**/pom.xml";

	public static final String				DEFAULT_GROUPID	= "${project.groupId}";

	public static final AntPathMatcher	pathMatcher			= new AntPathMatcher();

	/**
	 * 分析pom中的依赖关系
	 * 
	 * @param urls
	 * @param patterns
	 * @return
	 * @throws IOException
	 * @author 黄国庆 2012-4-28 上午8:45:20
	 */
	public static Map<JarURLConnection, ModulePluginDependency> analyzeDependendy(List<JarURLConnection> urls, List<String> patterns)
				throws IOException {
		List<Resource> resources = ResourceUtils.loadResources(urls,
			DEFAULT_MAVEN_POM);
		if (resources.size() != urls.size()) {
			throw new ModulePluginException("模块插件包中缺少pom.xml！");
		}
		Map<String, ModulePluginDependency> temp = new HashMap<String, ModulePluginDependency>();
		Map<String, ModulePluginDependency> allModulePluginDependency = new HashMap<String, ModulePluginDependency>();
		Map<JarURLConnection, ModulePluginDependency> needSort = new HashMap<JarURLConnection, ModulePluginDependency>();
		SystemLogUtils.secondPrintln("根据查询到的pom对模块插件jar进行排序前的预处理！");
		for (int i = 0; i < resources.size(); i++) {
			Resource resource = resources.get(i);
			// 创建一个新的SAXReader解析器
			SAXReader saxReader = new SAXReader();
			try {
				// 通过输入源构造一个Document
				Document document = saxReader.read(resource.getInputStream());
				Element root = document.getRootElement();
				ModulePluginDependency dependency = findModulePluginDependency(
					root, patterns, true, allModulePluginDependency);
				if (dependency == null) {
					continue;
				}
				needSort.put(urls.get(i), dependency);
				temp.put(dependency.key(), dependency);
				allModulePluginDependency.put(dependency.key(), dependency);
				// 查找依赖
				Element dependenciseElement = root.element("dependencies");
				if (dependenciseElement != null) {
					List<?> dependencise = dependenciseElement.elements("dependency");
					for (Object object : dependencise) {
						ModulePluginDependency child = findModulePluginDependency(
							(Element) object, patterns, false, null);
						if (child == null) {
							continue;
						}
						if (DEFAULT_GROUPID.equals(child.getGroupId())) {
							child.setGroupId(dependency.getGroupId());
						}
						dependency.putDependOn(child);
						if (allModulePluginDependency.containsKey(child.key())) {
							allModulePluginDependency.get(child.key()).refresh(child);
						} else {
							allModulePluginDependency.put(child.key(), child);
						}
					}
				}
			} catch (DocumentException e) {
				SystemLogUtils.errorPrintln(e.getMessage());
			}
			SystemLogUtils.thirdPrintln("解析Maven Pom：" + ResourceUtils.getShotFileName(resource));
		}
		SystemLogUtils.secondPrintln("对模块插件jar进行排序！");
		return needSort;
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
	private static ModulePluginDependency findModulePluginDependency(Element root, List<String> patterns, boolean findParent, Map<String, ModulePluginDependency> all) {
		// 过滤依赖中存在provided的scope
		if (!findParent) {
			Node scopeNode = root.element("scope");
			if (scopeNode != null && "provided".equals(scopeNode.getText())) {
				return null;
			}
		}
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
		ModulePluginDependency dependency = null;
		if (all != null) {
			dependency = all.get(ModulePluginDependency.key(path,
				artifactIdNode.getText()));
		}
		return dependency == null
					? new ModulePluginDependency(path, artifactIdNode.getText())
					: dependency;
	}

	/**
	 * 用patterns匹配依赖的groupId，过滤用
	 * 
	 * @param path
	 * @param patterns
	 * @return
	 * @author 黄国庆 2012-4-28 上午8:48:21
	 */
	private static boolean match(String path, List<String> patterns) {
		if (patterns == null || patterns.size() == 0) {
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
