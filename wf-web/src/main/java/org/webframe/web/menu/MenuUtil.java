
package org.webframe.web.menu;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.springframework.context.ApplicationContext;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.web.util.WebFrameUtils;

/**
 * 菜单工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午10:51:11
 * @version
 */
public class MenuUtil {

	/**
	 * 获取菜单json串；如果IMenuService实现类不存在，返回模块默认菜单
	 * 
	 * @param id
	 * @param level
	 * @return
	 * @author 黄国庆 2012-4-6 上午10:59:35
	 */
	public static String getMenusJson(String id, int level) {
		IMenuService menuService = getMenuService();
		List<? extends IMenu> menus = null;
		if (menuService != null) {
			menus = menuService.getMenus(id, level);
		} else {
			menus = getAllModulesDefaultMenu();
		}
		return convert(menus);
	}

	/**
	 * 从spring配置文件中获取IMenuService实现类
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 下午12:34:01
	 */
	private static IMenuService getMenuService() {
		ApplicationContext ac = WebFrameUtils.getApplicationContext();
		if (ac == null) return null;
		try {
			Map<String, IMenuService> menuMap = ac.getBeansOfType(IMenuService.class);
			return getSubMenuService(menuMap);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取所有模块的默认菜单集合
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 下午12:33:36
	 */
	private static List<? extends DefaultMenu> getAllModulesDefaultMenu() {
		Enumeration<IMenuSupport> enumeration = ModulePluginUtils.getDrivers(IMenuSupport.class);
		List<DefaultMenu> all = new ArrayList<DefaultMenu>();
		while (enumeration.hasMoreElements()) {
			IMenuSupport menuSupport = enumeration.nextElement();
			all.addAll(menuSupport.getModuleMenus());
		}
		return all;
	}

	/**
	 * 转换集合为json串
	 * 
	 * @param menus
	 * @return
	 * @author 黄国庆 2012-4-6 下午12:33:15
	 */
	private static String convert(List<? extends IMenu> menus) {
		if (menus == null) return "[]";
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {

			@Override
			public boolean apply(Object source, String name, Object value) {
				return !(name.equals("name") || name.equals("url") || name.equals("subMenus") || name.equals("parentId") || name.equals("id"));
			}
		});
		JSONArray jsonArray = JSONArray.fromObject(menus, jsonConfig);
		return jsonArray.toString();
	}

	/**
	 * 获取map集合中的超子类
	 * 
	 * @param menuMap
	 * @return
	 * @author 黄国庆 2012-4-6 下午12:32:42
	 */
	static IMenuService getSubMenuService(Map<String, IMenuService> menuMap) {
		int length = menuMap.size();
		if (length == 0) return null;
		Iterator<IMenuService> it = menuMap.values().iterator();
		IMenuService sub = null;
		while (it.hasNext()) {
			if (length == 1) return it.next();
			IMenuService temp = it.next();
			Class<? extends IMenuService> clazz = null;
			if (sub == null) {
				clazz = IMenuService.class;
			} else {
				clazz = sub.getClass();
			}
			if (clazz.isAssignableFrom(temp.getClass())) {
				sub = temp;
			}
		}
		return sub;
	}
}
