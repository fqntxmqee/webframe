
package org.webframe.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.webframe.core.model.action.ModuleActionType;
import org.webframe.core.model.form.ViewElement;

/**
 * 类功能描述：所有领域模型bean需要继承该BaseEntity
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-24
 *          下午02:39:13
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= -4374973980560730827L;

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
	 * 数据库非业务主键ID_
	 */
	private String									id;

	/**
	 * 名称
	 */
	private String									name;

	/**
	 * 排序序号
	 */
	private int										index;

	/**
	 * 使用状态，禁用或启用，默认为启用
	 */
	private boolean								enabeld;

	/**
	 * 创建时间，格式为：yyyy-MM-dd HH:mm:ss
	 */
	private String									createTime;

	/**
	 * 修改时间，格式为：yyyy-MM-dd HH:mm:ss
	 */
	private String									modifyTime;

	@Transient
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Transient
	public boolean isEnabled() {
		return enabeld;
	}

	public void setEnabled(boolean enabeld) {
		this.enabeld = enabeld;
	}

	@Transient
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Transient
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
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
