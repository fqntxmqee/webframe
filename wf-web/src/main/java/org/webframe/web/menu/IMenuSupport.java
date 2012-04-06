
package org.webframe.web.menu;

import java.util.List;

/**
 * 菜单支持接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午09:39:07
 * @version
 */
public interface IMenuSupport {

	/**
	 * 获取模块默认菜单，集合中的元素为顶级菜单
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 上午10:38:25
	 */
	List<? extends DefaultMenu> getModuleMenus();
}
