/*
 * wf-web-springmvc
 * Created on 2012-1-30-下午08:21:47
 */

package org.webframe.web.springmvc.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.model.IUUIDEntity;

/**
 * 测试持久类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午08:21:47
 * @version
 */
@Entity
@Table(name = "T_TEST")
public class TTest extends BaseEntity implements IUUIDEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3609673927622451841L;

	private boolean				enable				= false;

	private int						age					= 23;

	private double					xinzid				= 5000.23;

	private float					xinzif				= 3000.33f;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID_", length = 32, updatable = false, insertable = false)
	public String getId() {
		return id;
	}

	@Transient
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Transient
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Transient
	public double getXinzid() {
		return xinzid;
	}

	public void setXinzid(double xinzid) {
		this.xinzid = xinzid;
	}

	@Transient
	public float getXinzif() {
		return xinzif;
	}

	public void setXinzif(float xinzif) {
		this.xinzif = xinzif;
	}
}
