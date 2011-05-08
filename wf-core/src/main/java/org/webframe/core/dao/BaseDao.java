
package org.webframe.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.webframe.core.datasource.DataBaseType;
import org.webframe.core.hibernate.WFSessionFactoryWrapper;

/**
 * 类功能描述：DAO基类接口实现类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-21 下午09:14:16
 */
@Repository
public class BaseDao extends HibernateDaoSupport implements IBaseDao {

	protected Log	log	= LogFactory.getLog(getClass());

	@Autowired
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void saveOrUpdate(Object entity) {
		HibernateTemplate ht = getHibernateTemplate();
		ht.saveOrUpdate(entity);
	}

	@Override
	public void save(Object entity) {
		HibernateTemplate ht = getHibernateTemplate();
		ht.save(entity);
	}

	@Override
	public void saveOrUpdate(Collection<?> co) {
		HibernateTemplate ht = getHibernateTemplate();
		ht.saveOrUpdateAll(co);
	}

	@Override
	public void update(Object entity) {
		HibernateTemplate ht = getHibernateTemplate();
		ht.update(entity);
	}

	@Override
	public void update(String hql) {
		excuteHql(hql);
	}

	@Override
	public void delete(Object entity) {
		HibernateTemplate ht = getHibernateTemplate();
		ht.delete(entity);
	}

	@Override
	public void delete(String hql) {
		excuteHql(hql);
	}

	@Override
	public void deleteAll(Collection<?> collectionEntities) {
		super.getHibernateTemplate().deleteAll(collectionEntities);
	}

	@Override
	public void refresh(Object obj) {
		super.getHibernateTemplate().flush();
		super.getHibernateTemplate().refresh(obj);
	}

	@Override
	public Object load(Class<?> clazz, Serializable id) {
		return super.getHibernateTemplate().load(clazz, id);
	}

	@Override
	public List<?> loadAll(Class<?> entityClass) {
		return super.getHibernateTemplate().loadAll(entityClass);
	}

	@Override
	public Object get(Class<?> clazz, Serializable id) {
		return super.getHibernateTemplate().get(clazz, id);
	}

	@Override
	public Object loadNotLazy(Class<?> clazz, Serializable id) {
		Object o = get(clazz, id);
		super.getHibernateTemplate().initialize(o);
		return o;
	}

	@Override
	public Iterator<?> iterate(String hql) {
		return this.getHibernateTemplate().iterate(hql);
	}

	@Override
	public <X> X queryUniqueObject(String hql, Class<X> clazz) {
		@SuppressWarnings("unchecked")
		List<X> list = (List<X>) query(hql);
		if (list == null || list.size() == 0) return null;
		if (list.size() == 1) return list.get(0);
		throw new NonUniqueObjectException(hql, clazz.getName());
	}

	@Override
	public List<?> query(String hql) {
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<?> query(String hql, Object[] args, Type[] types) {
		Query query = this.getSession().createQuery(hql);
		query.setParameters(args, types);
		return query.list();
	}

	@Override
	public List<?> query(String hql, Integer maxSize) {
		Query query = this.getSession().createQuery(hql);
		if (maxSize != null && maxSize >= 0) {
			query.setMaxResults(maxSize);
		}
		return query.list();
	}

	@Override
	public List<?> queryByNamedParam(String hql, String[] args, Object[] values) {
		return this.getHibernateTemplate().findByNamedParam(hql, args, values);
	}

	@Override
	public List<?> findBySql(String sql, Object[] args, Type[] types) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setParameters(args, types);
		return query.list();
	}

	@Override
	public List<?> findBySql(String sql, int count) {
		return findBySql(sql, null, count);
	}

	@Override
	public List<?> findBySql(String sql) {
		return findBySql(sql, null, 0);
	}

	@Override
	public List<?> findBySql(String sql, Class<?> persistentClass, int count) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		if (persistentClass != null) {
			sqlQuery.addEntity(persistentClass);
		}
		synchronized (sqlQuery) {
			if (count > 0) {
				sqlQuery.setMaxResults(count);
			}
			return sqlQuery.list();
		}
	}

	@Override
	public void clear() {
		this.getSession().clear();
	}

	@Override
	public void evict(Object entity) {
		this.getSession().evict(entity);
	}

	@Override
	public void flush() {
		this.getSession().flush();
	}

	@Override
	public void evictSecondLevelCache(Class<?> persistentClass) {
		this.getSessionFactory().evict(persistentClass);
	}

	@Override
	public DataBaseType getDataBaseType() {
		SessionFactory sf = getSessionFactory();
		if (sf instanceof WFSessionFactoryWrapper) {
			WFSessionFactoryWrapper sfw = (WFSessionFactoryWrapper) sf;
			return sfw.getDatabaseType();
		}
		return DataBaseType.未知数据库;
	}

	/**
	 * @function: 删除和更新hql语句执行方法
	 * @param hql
	 * @author: 黄国庆 2011-3-22 下午05:06:21
	 */
	private void excuteHql(String hql) {
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
}
