
package org.webframe.web.menu;

import java.util.List;

/**
 * 菜单服务接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午10:48:22
 * @version
 */
public interface IMenuService {

	/**
	 * 获取菜单集合
	 * 
	 * @param id 父菜单id，当id为null时，返回一级菜单列表。
	 * @param level 子菜单层级数，当level小于等于0时，不返回子菜单数据，IMenu.getSubMenus()方法返回null。
	 * @return
	 * @author 黄国庆 2012-4-6 上午10:21:47
	 */
	List<IMenu> getMenus(String id, int level);
}
