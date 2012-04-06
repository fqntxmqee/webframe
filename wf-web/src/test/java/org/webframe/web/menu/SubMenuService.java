
package org.webframe.web.menu;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * SubMenuService
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 下午12:56:35
 * @version
 */
@Service
public class SubMenuService extends MenuService {

	@Override
	public List<IMenu> getMenus(String id, int level) {
		List<IMenu> list = super.getMenus(id, level);
		DefaultMenu parent = new DefaultMenu("http://localhost", "本地地址", 0);
		parent.addSubMenu(new DefaultMenu("/test", "测试", 0));
		list.add(parent);
		return list;
	}
}
