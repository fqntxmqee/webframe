
package org.webframe.web.menu;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;

import org.junit.Assert;
import org.junit.Test;
import org.webframe.test.BaseSpringTests;

/**
 * MenuUtilTest
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午11:25:45
 * @version
 */
public class MenuUtilTest extends BaseSpringTests {

	/**
	 * Test method for {@link org.webframe.web.menu.MenuUtil#getSubMenuService(java.util.Map)}.
	 */
	@Test
	public void testGetSubMenuService() {
		Map<String, IMenuService> menuMap = new TreeMap<String, IMenuService>();
		menuMap.put("1", new SubMenuService());
		menuMap.put("2", new MenuService());
		IMenuService menuService = MenuUtil.getSubMenuService(menuMap);
		Assert.assertEquals("获取菜单实例错误！", 1, menuService.getMenus("", 0).size());
	}

	/**
	 * Test method for {@link org.webframe.web.menu.MenuUtil#getMenusJson(String, int)}.
	 */
	@Test
	public void testGetMenusJson() {
		String json = MenuUtil.getMenusJson(null, 0);
		JSONArray jsonArray = JSONArray.fromObject(json);
		Collection<?> collection = JSONArray.toCollection(jsonArray, DefaultMenu.class);
		Assert.assertEquals("获取菜单数据错误！", 1, collection.size());
		System.out.println(json);
	}
}
