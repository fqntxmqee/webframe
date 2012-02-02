
package org.webframe.easy.model.action;

/**
 * 业务模块的默认动作类型，包括新增、修改、查询、删除、禁用、启用等功能
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-24
 *          上午10:10:37
 */
public enum ModuleActionType {
	新增("create", "新增"), 修改("update", "修改"), 查询("find", "查询"), 查看("view", "查看"), 删除("delete", "删除"), 禁用("disabled", "禁用"), 启用(
				"enabled", "启用"), 未知操作("unknown", "unknown");

	private final String	value;

	private final String	label;

	private ModuleActionType(String label, String value) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static ModuleActionType getName(String value) {
		for (ModuleActionType type : ModuleActionType.values()) {
			if (type.getValue().startsWith(value)) {
				return type;
			}
		}
		return ModuleActionType.未知操作;
	}

	public String toString() {
		return this.getValue();
	}
}
