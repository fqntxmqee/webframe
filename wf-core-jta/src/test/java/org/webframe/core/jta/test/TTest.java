/*
 * wf-core-jta
 * Created on 2011-6-29-下午05:12:19
 */

package org.webframe.core.jta.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.model.IUUIDEntity;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午05:12:19
 */
@Entity
@Table(name = "T_USER")
public class TTest extends BaseEntity implements IUUIDEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6569589359301294236L;

	private String					sex					= "man";

	private String					password;

	private String					likes;

	@Override
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID_", length = 32, updatable = false, insertable = false)
	public String getId() {
		return id;
	}

	@Override
	@Column(name = "NAME_", nullable = false)
	public String getName() {
		return super.getName();
	}

	@Override
	@Column(name = "INDEX_", nullable = false)
	public int getIndex() {
		return super.getIndex();
	}

	@Override
	@Column(name = "ENABLED_", nullable = false)
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	@Column(name = "CREATE_TIME_", nullable = false, updatable = false)
	public String getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@Column(name = "MODIFY_TIME_", insertable = false)
	public String getModifyTime() {
		return super.getModifyTime();
	}

	@Column(name = "SEX_", nullable = false)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "PASSWORD_", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "LIKES")
	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}
}
