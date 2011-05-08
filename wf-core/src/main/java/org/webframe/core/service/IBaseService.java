
package org.webframe.core.service;

import java.io.Serializable;

import org.webframe.core.model.BaseEntity;

/**
 * Service基类接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-21 下午09:33:46
 */
public interface IBaseService {

	/**
	 * 保存业务对象，如果entity为null，抛出EntityNullException； 捕获所有异常，抛出业务异常ServiceException
	 * 
	 * @param entity 业务对象
	 * @author 黄国庆 2011-3-23 下午08:42:35
	 */
	void save(BaseEntity entity);

	/**
	 * 更新业务对象，如果entity为null，抛出EntityNullException； 捕获所有异常，抛出业务异常ServiceException
	 * 
	 * @param entity 业务对象
	 * @author 黄国庆 2011-3-23 下午08:43:01
	 */
	void update(BaseEntity entity);

	/**
	 * 保存或修改业务对象，如果entity为null，抛出EntityNullException； 捕获所有异常，抛出业务异常ServiceException
	 * 
	 * @param entity 业务对象
	 * @author 黄国庆 2011-3-23 下午08:43:05
	 */
	void saveOrUpdate(BaseEntity entity);

	/**
	 * 删除业务对象，如果entity为null，抛出EntityNullException； 捕获所有异常，抛出业务异常ServiceException
	 * 
	 * @param entity 持久化业务对象
	 * @author 黄国庆 2011-3-23 下午08:43:12
	 */
	void delete(BaseEntity entity);

	/**
	 * 根据指定业务对象class类型，主键id删除对象，该方法先通过get(Class<?> clazz, Serializable id)
	 * 方法加载持久化对象，如果持久化对象不存在抛出EntityNullException；存在直接删除。
	 * 
	 * @param clazz 业务对象class
	 * @param id 主键id
	 * @author 黄国庆 2011-3-24 上午10:50:43
	 */
	void delete(Class<?> clazz, Serializable id);

	/**
	 * 对象加载 默认延迟加载，找不到对象返回null
	 * 
	 * @param clazz
	 * @param id
	 * @return null or Object
	 * @author 黄国庆 2010-7-6 下午02:01:59
	 */
	Object get(Class<?> clazz, Serializable id);

	/**
	 * 通过资源id获取对象，若找不到实体，不返回null，返回代理， 当使用属性的时候会抛出异常
	 * 
	 * @param clazz
	 * @param id
	 * @return 代理后的实体
	 * @author 黄国庆 2010-7-6 下午02:02:47
	 */
	Object load(Class<?> clazz, Serializable id);
}
