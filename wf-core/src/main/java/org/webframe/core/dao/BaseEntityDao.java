
package org.webframe.core.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.NonUniqueObjectException;
import org.springframework.stereotype.Repository;
import org.webframe.core.exception.entity.EntityException;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.ReflectionUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-25
 *          上午10:51:05
 */
@Repository
@SuppressWarnings("unchecked")
public class BaseEntityDao<T extends BaseEntity> extends BaseDao implements IBaseEntityDao<T> {

	private Class<T>	entityClass;

	public BaseEntityDao() {
		entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	protected final Class<T> getEntityClass() {
		return this.entityClass;
	}

	@Override
	public T findEntity(final Serializable id) throws EntityException {
		return findEntity(getEntityClass(), id);
	}

	@Override
	public T findEntity(Class<T> entityClass, final Serializable id) throws EntityException {
		throwEntityException(entityClass);
		return (T) super.get(entityClass, id);
	}

	@Override
	public void saveEntity(T entity) {
		super.save(entity);
	}

	@Override
	public void saveOrUpdateEntity(T entity) {
		super.saveOrUpdate(entity);
	}

	@Override
	public void updateEntity(T entity) {
		super.update(entity);
	}

	@Override
	public void deleteEntity(T entity) {
		super.delete(entity);
	}

	@Override
	public List<T> findByExample(T entity) {
		return super.getHibernateTemplate().findByExample(entity);
	}

	@Override
	public void deleteEntity(Serializable id) throws EntityException {
		delete(findEntity(id));
	}

	@Override
	public List<T> findAll() throws EntityException {
		throwEntityException(getEntityClass());
		return (List<T>) super.loadAll(getEntityClass());
	}

	@Override
	public List<T> findByProperty(String propertyName, Object value) throws EntityException {
		return findByProperty(getEntityClass(), propertyName, value);
	}

	@Override
	public List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) throws EntityException {
		throwEntityException(entityClass);
		String queryString = "from " + entityClass.getName() + " as model where model." + propertyName + "=?";
		return super.getHibernateTemplate().find(queryString, value);
	}

	@Override
	public T findByUniqueProperty(String propertyName, Object value) throws EntityException {
		return findByUniqueProperty(getEntityClass(), propertyName, value);
	}

	@Override
	public T findByUniqueProperty(Class<T> entityClass, String propertyName, Object value) throws EntityException {
		List<T> list = findByProperty(entityClass, propertyName, value);
		if (list == null || list.isEmpty()) return null;
		if (list.size() == 1) return list.get(0);
		throw new NonUniqueObjectException(propertyName, entityClass.getName());
	}

	private void throwEntityException(Class<T> entityClass) throws EntityException {
		if (Object.class.getName().equals(entityClass.getName())
					|| BaseEntity.class.getName().equals(entityClass.getName())) {
			throw new EntityException("默认BaseEntityDao实例，不支持所有依赖entityClass的方法操作！");
		}
	}
}
