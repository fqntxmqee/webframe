
package org.webframe.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.type.Type;
import org.webframe.core.datasource.DataBaseType;

/**
 * DAO基类接口，提供数据库的增删改查操作
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-21 下午09:14:31
 */
public interface IBaseDao {

	/**
	 * 新增或更新
	 * 
	 * @param entity
	 * @author 黄国庆 2010-7-6 下午02:27:20
	 */
	void saveOrUpdate(Object entity);

	/**
	 * 新增
	 * 
	 * @param entity
	 * @author 黄国庆 2010-7-6 下午02:27:58
	 */
	void save(Object entity);

	/**
	 * 更新
	 * 
	 * @param entity
	 * @author 黄国庆 2010-7-6 下午02:28:07
	 */
	void update(Object entity);

	/**
	 * 通过hql进行批量更新
	 * 
	 * @param hql hql更新语句，例如："update TTestUser set modifyTime=''"
	 * @author 黄国庆 2010-7-6 下午02:23:58
	 */
	void update(String hql);

	/**
	 * 批量新增或更新
	 * 
	 * @param collection
	 * @author 黄国庆 2010-7-6 下午02:28:47
	 */
	void saveOrUpdate(Collection<?> collection);

	/**
	 * 删除
	 * 
	 * @param entity 持久化对象
	 * @author 黄国庆 2010-7-6 下午02:30:03
	 */
	void delete(Object entity);

	/**
	 * 通过hql语句删除
	 * 
	 * @param hql hql删除语句，例如："delete from TTestUser where id='" + id + "'"
	 * @author 黄国庆 2010-7-6 下午02:37:41
	 */
	void delete(String hql);

	/**
	 * 批量删除
	 * 
	 * @param collectionEntities 持久化对象集合
	 * @author 黄国庆 2010-7-6 下午02:30:13
	 */
	void deleteAll(Collection<?> collectionEntities);

	/**
	 * 首先删除内存中的所有对象，再加载对象
	 * 
	 * @param entity
	 * @author 黄国庆 2010-7-6 下午02:38:24
	 */
	void refresh(Object entity);

	/**
	 * 通过资源id获取对象，若找不到实体，则返回异常
	 * 
	 * @param clazz 持久化对象Class
	 * @param id 主键
	 * @return 不为null
	 * @author 黄国庆 2010-7-6 下午02:29:24
	 */
	Object load(Class<?> clazz, Serializable id);

	/**
	 * 查询BaseObject 指定子类所有对象
	 * 
	 * @param entityClass 持久化对象Class
	 * @return 不为null
	 * @author 黄国庆 2010-7-6 下午02:32:16
	 */
	List<?> loadAll(Class<?> entityClass);

	/**
	 * 对象加载 默认延迟加载，找不到对象返回null
	 * 
	 * @param clazz 持久化对象Class
	 * @param id 主键
	 * @return null or Object
	 * @author 黄国庆 2010-7-6 下午02:35:04
	 */
	Object get(Class<?> clazz, Serializable id);

	/**
	 * 加载对象的所有属性
	 * 
	 * @param clazz 持久化对象Class
	 * @param id 主键
	 * @return 返回null或者持久化对象，持久化对象的属性不延迟加载
	 * @author 黄国庆 2010-7-6 下午02:36:16
	 */
	Object loadNotLazy(Class<?> clazz, Serializable id);

	/**
	 * : 加载对象集合，会级联查询，不延迟加载！
	 * 
	 * @param hql hql查询语句，例如："from TTestUser"
	 * @return
	 * @author 黄国庆 2011-3-23 下午09:15:55
	 */
	Iterator<?> iterate(String hql);

	/**
	 * : 可以根据唯一属性进行查询，如果指定属性值的数据库记录不止一条， 则抛出NonUniqueObjectException异常，如果指定属性值的数据库记录不存在，
	 * 返回null，只有一条记录，返回持久化对象。
	 * 
	 * @param hql hql查询语句，例如："from TTestUser where name='" + testUserName + "'"
	 * @param clazz 持久化对象Class，与hql中的持久化对象一致
	 * @return null、NonUniqueObjectException、Entity
	 * @author 黄国庆 2011-3-23 下午09:18:16
	 */
	<X> X queryUniqueObject(String hql, Class<X> clazz);

	/**
	 * : 查询数据记录，返回集合
	 * 
	 * @param hql hql查询语句，例如："from TTestUser"
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午09:24:08
	 */
	List<?> query(String hql);

	/**
	 * : 查询数据记录，返回集合，动态指定查询条件
	 * 
	 * @param hql hql查询语句，例如："from TTestUser u where u.name = ? and u.enabled is ?"
	 * @param args 参数值，数组值按照问号的顺序一一对应，例如：Object[] values = {"testUserName", true}
	 * @param types 参数类型，数组值按照问号的顺序一一对应，例如：Type[] types = {Hibernate.STRING, Hibernate.BOOLEAN}
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午09:25:35
	 */
	List<?> query(String hql, Object[] args, Type[] types);

	/**
	 * : 查询数据记录，返回集合，可以指定最大查询记录条数
	 * 
	 * @param hql hql查询语句，例如："from TTestUser"
	 * @param maxSize 如果为null 或 <=0，则查询所有符合条件的记录
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午09:25:47
	 */
	List<?> query(String hql, Integer maxSize);

	/**
	 * : 查询数据记录，返回集合，动态指定查询条件
	 * 
	 * @param hql hql查询语句，例如："from TTestUser u where u.name = :name and u.enabled is :enabled"
	 * @param args 参数名，例如：String[] args = {"name", "enabled"}
	 * @param values 参数值，例如：Object[] values = {"testUserName", true};
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午09:53:02
	 */
	List<?> queryByNamedParam(String hql, String[] args, Object[] values);

	/**
	 * : 查询数据记录，返回集合，动态指定查询条件
	 * 
	 * @param sql sql查询语句，例如："select * from T_TEST_USER u where u.NAME_ = ? and u.ENABLED_ is ?"
	 * @param args 参数值，数组值按照问号的顺序一一对应，例如：Object[] values = {"testUserName", true}
	 * @param types 参数类型，数组值按照问号的顺序一一对应，例如：Type[] types = {Hibernate.STRING, Hibernate.BOOLEAN}
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午10:03:51
	 */
	List<?> findBySql(String sql, Object[] args, Type[] types);

	/**
	 * : 查询数据记录，返回集合
	 * 
	 * @param sql sql查询语句，例如：String sql = "select * from T_TEST_USER"
	 * @param count 值<=0时，查询所有符合条件的记录
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午10:14:19
	 */
	List<?> findBySql(String sql, int count);

	/**
	 * : 查询数据记录，返回集合
	 * 
	 * @param sql sql查询语句，例如：String sql = "select * from T_TEST_USER"
	 * @return not null
	 * @author 黄国庆 2011-3-23 下午10:18:44
	 */
	List<?> findBySql(String sql);

	/**
	 * 查询数据记录，返回集合
	 * 
	 * @param sql sql查询语句，例如：String sql = "select * from T_TEST_USER";
	 * @param persistentClass pojo对象，如果为null，List集合中为Object对象
	 * @param count 查询条数，如果小于等于0，查询所有记录
	 * @return not null
	 * @author 黄国庆 2010-7-9 上午11:00:59
	 */
	List<?> findBySql(String sql, Class<?> persistentClass, int count);

	/**
	 * 从SessionFactory中清楚指定类型的持久性对象
	 * 
	 * @param persistentClass 持久性对象class
	 * @author 黄国庆 2011-3-23 下午10:45:51
	 */
	void evictSecondLevelCache(Class<?> persistentClass);

	/**
	 * 从session中清除指定的缓冲对象，
	 * 
	 * @param entity 持久化对象
	 * @author 黄国庆 2011-3-23 下午10:44:44
	 */
	void evict(Object entity);

	/**
	 * : 把缓冲区内的对象全部清除，不包括操作中的对象
	 * 
	 * @author 黄国庆 2011-3-23 下午10:45:32
	 */
	void clear();

	/**
	 * 把缓冲区内的对象全部提交，一般在进行批量数据处理的时候， 配合使用clear()方法，例如： <pre> for (int i = 0; i < 100000; i++) {
	 * baseDao.save(po); if (i % 50 == 0) { // flush a batch of inserts and release memory
	 * baseDao.flush(); baseDao.clear(); } } </pre>
	 * 
	 * @author 黄国庆 2011-3-24 上午10:10:21
	 */
	void flush();

	/**
	 * 获取连接数据库类型DataBaseType
	 * 
	 * @return 返回DataBaseType
	 * @author 黄国庆 2010-12-17 下午04:12:03
	 */
	DataBaseType getDataBaseType();
}
