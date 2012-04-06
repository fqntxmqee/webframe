
package org.webframe.web.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认菜单
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午10:30:55
 * @version
 */
public class DefaultMenu implements IMenu {

	private String					url		= null;

	private String					name		= null;

	private int						index		= 0;

	private List<DefaultMenu>	subMenus	= null;

	public DefaultMenu() {
	}

	public DefaultMenu(String url, String name) {
		this(url, name, 0);
	}

	public DefaultMenu(String url, String name, int index) {
		setUrl(url);
		setName(name);
		setIndex(index);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getCode() {
		return name;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getParentId() {
		return null;
	}

	@Override
	public int getChildCount() {
		return subMenus == null ? 0 : subMenus.size();
	}

	@Override
	public boolean isLeaf() {
		return getChildCount() == 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public List<DefaultMenu> getSubMenus() {
		return subMenus;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSubMenus(List<DefaultMenu> subMenus) {
		this.subMenus = subMenus;
	}

	public void addSubMenu(DefaultMenu subMenu) {
		if (this.subMenus == null) {
			this.subMenus = new ArrayList<DefaultMenu>();
		}
		this.subMenus.add(subMenu);
	}
}
