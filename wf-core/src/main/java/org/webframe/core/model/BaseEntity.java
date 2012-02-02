
package org.webframe.core.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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
	private static final long	serialVersionUID	= -4374973980560730827L;

	/**
	 * 数据库非业务主键ID_
	 */
	protected String				id;

	/**
	 * 名称
	 */
	private String					name;

	/**
	 * 排序序号
	 */
	private int						index;

	/**
	 * 使用状态，禁用或启用，默认为启用
	 */
	private boolean				enabeld;

	/**
	 * 创建时间，格式为：yyyy-MM-dd HH:mm:ss
	 */
	private String					createTime;

	/**
	 * 修改时间，格式为：yyyy-MM-dd HH:mm:ss
	 */
	private String					modifyTime;

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
}
