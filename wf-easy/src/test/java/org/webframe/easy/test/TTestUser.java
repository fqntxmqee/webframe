
package org.webframe.easy.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.webframe.core.model.IUUIDEntity;
import org.webframe.easy.model.EasyEntity;
import org.webframe.easy.model.action.ModuleActionType;
import org.webframe.easy.model.form.ViewElement;
import org.webframe.easy.model.form.ViewElement.QueryConditionType;
import org.webframe.easy.model.form.ViewElementType;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-21 下午10:40:22
 */
@Entity
@Table(name = "T_TEST_USER")
public class TTestUser extends EasyEntity implements IUUIDEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5058630216253379749L;

	private String					sex					= "man";

	private String					password;

	private String					likes;

	TTestUser						testUser				= null;

	List<String>					list					= new ArrayList<String>();

	public void addList(String name) {
		list.add(name);
	}

	public TTestUser() {
	}

	public TTestUser(TTestUser testUser) {
		this.testUser = testUser;
	}

	@Override
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

	@Override
	public List<ViewElement> viewElementList() {
		List<ViewElement> viewElements = super.viewElementList();
		ViewElement viewElement;
		viewElements.add(new ViewElement("名称", "name", ViewElementType.TEXT, ""));
		viewElements.add(new ViewElement("密码", "password", ViewElementType.PASSWORD, "").setListPageConfig(false, false,
			null));
		viewElements.add(new ViewElement("排序", "index", ViewElementType.TEXT, "").setListPageConfig(true, false, null));
		viewElement = new ViewElement("状态", "enabled", ViewElementType.SELECT, "");
		viewElement.setValueMap(new HashMap<String, Object>() {

			private static final long	serialVersionUID	= -4441777963201624419L;
			{
				put("true", "启用");
				put("false", "禁用");
			}
		});
		viewElements.add(viewElement.setListPageConfig(true, true, QueryConditionType.布尔类型));
		viewElement = new ViewElement("性别", "sex", ViewElementType.SELECT, "");
		viewElement.setValueMap(new HashMap<String, Object>() {

			private static final long	serialVersionUID	= -4441777963201624419L;
			{
				put("man", "男");
				put("woman", "女");
			}
		});
		viewElements.add(viewElement.setListPageConfig(true, true, QueryConditionType.等值类型));
		viewElements.add(new ViewElement("爱好", "likes", ViewElementType.TEXT, "").setListPageConfig(true, false, null));
		viewElements.add(new ViewElement("创建时间", "createTime", ViewElementType.TEXT, "").setFormed(false)
			.setListPageConfig(true, true, QueryConditionType.区间类型));
		viewElements.add(new ViewElement("修改时间", "modifyTime", ViewElementType.TEXT, "").setFormed(false)
			.setListPageConfig(false, false, null));
		return viewElements;
	}

	@Override
	protected ModuleActionType[] excludeModuleActionTypes() {
		return new ModuleActionType[]{};
	}
}
