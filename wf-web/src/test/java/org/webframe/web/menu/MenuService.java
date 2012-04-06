
package org.webframe.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * MenuService
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 下午12:56:20
 * @version
 */
@Service
public class MenuService implements IMenuService {

	@Override
	public List<IMenu> getMenus(String id, int level) {
		return new ArrayList<IMenu>();
	}
}
