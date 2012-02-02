/*
 * wf-easy
 * Created on 2012-2-2-上午09:24:25
 */

package org.webframe.easy.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.webframe.core.model.BaseEntity;
import org.webframe.easy.model.action.ModuleActionType;
import org.webframe.easy.model.form.ViewElement;

/**
 * 简单模块实体类基类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-2 上午09:24:25
 * @version
 */
public class EasyEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= -108473429165892879L;

	private static Set<ModuleActionType>	defaultActions		= new HashSet<ModuleActionType>(7);
	static {
		defaultActions.add(ModuleActionType.新增);
		defaultActions.add(ModuleActionType.修改);
		defaultActions.add(ModuleActionType.查询);
		defaultActions.add(ModuleActionType.查看);
		defaultActions.add(ModuleActionType.删除);
		defaultActions.add(ModuleActionType.禁用);
		defaultActions.add(ModuleActionType.启用);
	}

	/**
	 * 获取页面模板所有可视化数据，通过页面元素集合，可以根据模板生成form表单 页面、修改页面、查询页面，包括查询条件的指定等等。
	 * 
	 * @return 返回页面元素集合，不返回null
	 * @author: 黄国庆 2011-1-22 下午12:23:17
	 */
	public List<ViewElement> viewElementList() {
		List<ViewElement> formElements = new ArrayList<ViewElement>();
		formElements.add(new ViewElement());
		return formElements;
	}

	/**
	 * @function: 获取业务模型的动作，默认提供七种动作： ModuleActionType.新增、ModuleActionType.修改、
	 *            ModuleActionType.查询、ModuleActionType.查看、 ModuleActionType.删除、ModuleActionType.禁用、
	 *            ModuleActionType.启用
	 * @return 返回七种动作的Set集合
	 * @author: 黄国庆 2011-3-23 下午08:52:12
	 */
	public final Set<ModuleActionType> defaultModuleActionTypes() {
		Set<ModuleActionType> result = new HashSet<ModuleActionType>(7);
		ModuleActionType[] excludes = excludeModuleActionTypes();
		if (excludes != null) {
			List<ModuleActionType> excludesList = Arrays.asList(excludes);
			for (ModuleActionType defaultAction : defaultActions) {
				if (!excludesList.contains(defaultAction)) {
					result.add(defaultAction);
				}
			}
		}
		return result;
	}

	/**
	 * @function: 该方法可以被子类重新，返回的数组，集合用于从默认业务模型动作中exclude 指定几种动作的数组集合
	 * @return 返回业务模型动作的数组
	 * @author: 黄国庆 2011-3-23 下午08:55:05
	 */
	protected ModuleActionType[] excludeModuleActionTypes() {
		return new ModuleActionType[0];
	}
}
