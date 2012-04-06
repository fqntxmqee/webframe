
package org.webframe.web.menu;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.webframe.core.model.BaseEntity;

/**
 * 抽象菜单
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午09:51:40
 * @version
 */
@MappedSuperclass
public abstract class AbstractMenu extends BaseEntity implements IMenu {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6956285716250960223L;

	private String					url					= null;

	private String					parentId				= null;

	private List<AbstractMenu>	subMenus				= null;

	@Override
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID_", length = 32, updatable = false, insertable = false)
	public String getId() {
		return id;
	}

	@Override
	@Column(name = "PARENT_ID_", length = 32)
	public String getParentId() {
		return parentId;
	}

	@Override
	@Column(name = "NAME_", nullable = false)
	public String getName() {
		return super.getName();
	}

	@Override
	@Column(name = "URL_", nullable = false, unique = true)
	public String getUrl() {
		return url;
	}

	@Override
	@Column(name = "ENABLED_", nullable = false)
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	@Column(name = "INDEX_", nullable = false)
	public int getIndex() {
		return super.getIndex();
	}

	@Override
	@Transient
	public List<AbstractMenu> getSubMenus() {
		return subMenus;
	}

	@Override
	@Transient
	public String getCode() {
		return getUrl();
	}

	@Override
	@Transient
	public int getLevel() {
		return -1;
	}

	@Override
	@Transient
	public int getChildCount() {
		return -1;
	}

	@Override
	@Transient
	public boolean isLeaf() {
		return false;
	}

	@Override
	@Transient
	public String getDescription() {
		return null;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setSubMenus(List<AbstractMenu> subMenus) {
		this.subMenus = subMenus;
	}
}
