
package org.webframe.core.service;

import java.io.Serializable;

import org.webframe.core.model.BaseEntity;

/**
 * 支持泛型的Service基类接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-25
 *          下午02:27:40
 */
public interface IBaseEntityService<T extends BaseEntity> extends IBaseService {

	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 * @author 黄国庆 2011-3-25 上午10:35:44
	 */
	void saveEntity(T entity);

	/**
	 * 修改实体对象
	 * 
	 * @param entity
	 * @author 黄国庆 2011-3-25 上午10:35:56
	 */
	void updateEntity(T entity);

	/**
	 * 保存或修改实体对象
	 * 
	 * @param entity
	 * @author 黄国庆 2011-3-25 上午10:36:05
	 */
	void saveOrUpdateEntity(T entity);

	/**
	 * 删除实体对象
	 * 
	 * @param entity
	 * @author 黄国庆 2011-3-25 上午10:36:20
	 */
	void deleteEntity(T entity);

	/**
	 * 根据主键id查询实体对象
	 * 
	 * @param entityClass 实体对象class
	 * @param id 主键id
	 * @return Null or 实体对象
	 * @author 黄国庆 2011-3-25 上午10:36:31
	 */
	T findEntity(Class<T> entityClass, Serializable id);

	/**
	 * 根据主键id查询实体对象
	 * 
	 * @param id 主键id
	 * @return Null or 实体对象
	 * @author 黄国庆 2011-3-25 上午10:38:40
	 */
	T findEntity(Serializable id);
}
