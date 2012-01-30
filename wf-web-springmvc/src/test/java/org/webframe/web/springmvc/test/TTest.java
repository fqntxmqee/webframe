/*
 * wf-web-springmvc
 * Created on 2012-1-30-下午08:21:47
 */

package org.webframe.web.springmvc.test;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.webframe.core.model.UUIDEntity;

/**
 * 测试持久类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午08:21:47
 * @version
 */
@Entity
@Table(name = "T_TEST")
public class TTest extends UUIDEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3609673927622451841L;

	private boolean				enable				= false;

	private int						age					= 23;

	private double					xinzid				= 5000.23;

	private float					xinzif				= 3000.33f;

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
