
package org.webframe.web.menu;

import java.util.List;

import org.webframe.core.model.ITreeEntity;
import org.webframe.core.model.IUUIDEntity;

/**
 * 菜单接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-4-6 上午09:40:53
 * @version
 */
public interface IMenu extends IUUIDEntity, ITreeEntity {

	/**
	 * 菜单名称
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 上午09:55:06
	 */
	String getName();

	/**
	 * 菜单路径
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 上午09:55:15
	 */
	String getUrl();

	/**
	 * 菜单位置
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 上午09:55:26
	 */
	int getIndex();

	/**
	 * 子菜单集合
	 * 
	 * @return
	 * @author 黄国庆 2012-4-6 上午10:18:53
	 */
	List<? extends IMenu> getSubMenus();
}
