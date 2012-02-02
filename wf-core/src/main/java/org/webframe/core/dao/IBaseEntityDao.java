
package org.webframe.core.dao;

import java.io.Serializable;
import java.util.List;

import org.webframe.core.exception.entity.EntityException;
import org.webframe.core.model.BaseEntity;

/**
 * 支持泛型的Dao，该接口继承IBaseDao
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-25
 *          上午10:50:20
 */
public interface IBaseEntityDao<T extends BaseEntity> extends IBaseDao {

	BaseEntityDao<T> getSubClassEntityDao(Class<T> entityClass);

	/**
	 * 保存实体对象
	 * 
	 * @param entity 实体对象
	 * @author 黄国庆 2011-3-25 上午09:50:56
	 */
	void saveEntity(T entity);

	/**
	 * 保存或修改实体对象
	 * 
	 * @param entity 实体对象
	 * @author 黄国庆 2011-3-25 上午09:52:15
	 */
	void saveOrUpdateEntity(T entity);

	/**
	 * 修改实体对象
	 * 
	 * @param entity 实体对象
	 * @author 黄国庆 2011-3-25 上午09:52:36
	 */
	void updateEntity(T entity);

	/**
	 * 删除实体对象
	 * 
	 * @param entity 实体对象
	 * @author 黄国庆 2011-3-25 上午09:52:49
	 */
	void deleteEntity(T entity);

	/**
	 * 根据指定主键id，删除实体对象
	 * 
	 * @param id 主键id
	 * @author 黄国庆 2011-3-25 上午09:53:02
	 * @throws EntityException
	 */
	void deleteEntity(Serializable id) throws EntityException;

	/**
	 * 根据指定主键id，查询实体对象
	 * 
	 * @param id 主键id
	 * @return Null or Entity
	 * @author 黄国庆 2011-3-25 上午09:53:49
	 * @throws EntityException
	 */
	T findEntity(Serializable id) throws EntityException;

	/**
	 * 根据指定主键id，查询实体对象
	 * 
	 * @param entityClass 实体对象class
	 * @param id 主键id
	 * @return Null or Entity
	 * @author 黄国庆 2011-3-25 上午10:58:32
	 * @throws EntityException
	 */
	T findEntity(Class<T> entityClass, final Serializable id) throws EntityException;

	/**
	 * 模糊查询指定条件对象集合 <br> 用法：可以实例化一个空的T对象，需要查询某个字段，就set该字段的条件然后调用本方法<br>
	 * 缺点：目前测试貌似只能支持String的模糊查询，虽然有办法重写，但没必要，其他用HQL<br>
	 * 
	 * @param entity 条件实体
	 * @return 结合
	 */
	List<T> findByExample(T entity);

	/**
	 * 查询泛型实体所有数据库记录集合
	 * 
	 * @return 不为null的list集合
	 * @author 黄国庆 2011-3-25 上午09:54:41
	 * @throws EntityException
	 */
	List<T> findAll() throws EntityException;

	/**
	 * 查找指定实体属性的实体集合
	 * 
	 * @param entityClass 实体class
	 * @param propertyName 属性名
	 * @param value 条件
	 * @return 实体集合
	 * @author 黄国庆 2011-3-25 上午09:54:41
	 * @throws EntityException
	 */
	List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) throws EntityException;

	/**
	 * 查找指定实体属性的实体集合
	 * 
	 * @param propertyName 属性名
	 * @param value 条件
	 * @return 实体集合
	 * @author 黄国庆 2011-3-25 上午09:56:03
	 * @throws EntityException
	 */
	List<T> findByProperty(String propertyName, Object value) throws EntityException;

	/**
	 * 可以根据唯一属性进行查询，如果指定属性值的数据库记录不止一条， 则抛出NonUniqueObjectException异常，如果指定属性值的数据库记录不存在，
	 * 返回null，只有一条记录，返回持久化对象。
	 * 
	 * @param entityClass 实体class
	 * @param propertyName 属性名
	 * @param value 条件
	 * @return 唯一实体对象 or Null
	 * @author 黄国庆 2011-3-25 上午09:56:33
	 * @throws EntityException
	 */
	T findByUniqueProperty(Class<T> entityClass, String propertyName, Object value) throws EntityException;

	/**
	 * 可以根据唯一属性进行查询，如果指定属性值的数据库记录不止一条， 则抛出NonUniqueObjectException异常，如果指定属性值的数据库记录不存在，
	 * 返回null，只有一条记录，返回持久化对象。
	 * 
	 * @param propertyName 属性名
	 * @param value 条件
	 * @return 唯一实体对象 or Null
	 * @author 黄国庆 2011-3-25 上午09:56:36
	 * @throws EntityException
	 */
	T findByUniqueProperty(String propertyName, Object value) throws EntityException;
}
