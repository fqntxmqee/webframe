package org.webframe.support.driver.relation;

import java.util.HashMap;
import java.util.Map;


/**
 * 模块依赖信息
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-4-27 上午11:16:26
 * @version
 */
public class ModulePluginDependency implements
			Comparable<ModulePluginDependency> {

	private String											groupId;

	private String											artifactId;

	private int												index			= 0;

	private Map<String, ModulePluginDependency>	dependOnMap	= null;

	public ModulePluginDependency(String groupId, String artifactId) {
		this.groupId = groupId;
		this.artifactId = artifactId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public Map<String, ModulePluginDependency> getDependOnMap() {
		return dependOnMap;
	}

	public void setDependOnMap(Map<String, ModulePluginDependency> dependOnMap) {
		this.dependOnMap = dependOnMap;
	}

	public void putDependOn(ModulePluginDependency dependency) {
		if (dependOnMap == null) {
			dependOnMap = new HashMap<String, ModulePluginDependency>();
		}
		String key = dependency.toString();
		if (key != null && !"".equals(key)) {
			dependOnMap.put(dependency.toString(), dependency);
		}
	}

	public String key() {
		if (groupId == null || "".equals(groupId)) {
			throw new IllegalArgumentException("groupId 不能为空或null！");
		}
		if (artifactId == null || "".equals(artifactId)) {
			throw new IllegalArgumentException("artifactId 不能为空或null！");
		}
		return groupId + "." + artifactId;
	}

	public int increase() {
		return increase(1);
	}

	public int increase(int index) {
		this.index += index;
		if (dependOnMap != null) {
			for (String key : dependOnMap.keySet()) {
				dependOnMap.get(key).increase(index);
			}
		}
		return this.index;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public String toString() {
		return key() + ":deep:" + getIndex();
	}

	@Override
	public int compareTo(ModulePluginDependency o) {
		if (o == null) {
			return -1;
		}
		if (getIndex() > o.getIndex()) {
			return -1;
		} else if (getIndex() == o.getIndex()) {
			return 0;
		} else {
			return 1;
		}
	}
}
