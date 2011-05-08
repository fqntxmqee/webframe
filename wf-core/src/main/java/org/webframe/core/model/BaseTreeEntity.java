
package org.webframe.core.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 类功能描述：所有具有树形特征的领域模型bean需要继承该BaseTreeBean
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-24
 *          下午02:45:51
 */
@MappedSuperclass
public abstract class BaseTreeEntity extends UUIDEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3833607575688637867L;

	/**
	 * 编码，业务主键
	 */
	private String					code;

	/**
	 * 所在层级
	 */
	private Integer				level;

	/**
	 * 描述
	 */
	private String					description;

	/**
	 * 父节点的id
	 */
	private String					parentId;

	/**
	 * 孩子节点的数目
	 */
	private int						childCount;

	/**
	 * 是否叶子节点
	 */
	private boolean				leaf;

	@Transient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Transient
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Transient
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Transient
	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	@Transient
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
}
