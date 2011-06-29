
package org.webframe.core.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webframe.core.dao.IBaseDao;
import org.webframe.core.dao.IBaseEntityDao;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.exception.entity.EntityException;
import org.webframe.core.exception.entity.EntityExistException;
import org.webframe.core.exception.entity.EntityNullException;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.util.ReflectionUtils;

/**
 * 泛型基类Service实现类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-25
 *          下午02:28:02
 */
@Service
public class BaseEntityService<T extends BaseEntity> extends BaseService implements IBaseEntityService<T> {

	@Autowired
	private IBaseEntityDao<T>	baseEntityDao;

	private Class<T>				entityClass;

	public BaseEntityService() {
		entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	protected final Class<T> getEntityClass() {
		return this.entityClass;
	}

	protected IBaseEntityDao<T> getBaseEntityDao() {
		return baseEntityDao;
	}

	@Override
	protected IBaseDao getBaseDao() {
		return getBaseEntityDao();
	}

	@Override
	public void saveEntity(T entity) throws ServiceException {
		super.save(entity);
	}

	@Override
	public void saveOrUpdateEntity(T entity) throws ServiceException {
		super.saveOrUpdate(entity);
	}

	@Override
	public void updateEntity(T entity) throws ServiceException {
		super.update(entity);
	}

	@Override
	public void deleteEntity(T entity) throws ServiceException {
		super.delete(entity);
	}

	@Override
	public T findEntity(Class<T> entityClass, Serializable id) throws EntityException {
		return getBaseEntityDao().findEntity(entityClass, id);
	}

	@Override
	public T findEntity(Serializable id) throws EntityException {
		return findEntity(getEntityClass(), id);
	}

	/**
	 * 验证Entity是否为null，如果为null抛出EntityNullException
	 * 
	 * @param entity
	 * @author: 黄国庆 2010-12-27 下午02:59:09
	 * @throws EntityNullException
	 */
	protected void entityNullValidate(BaseEntity entity) throws EntityNullException {
		if (entity == null) throw new EntityNullException(getEntityClass());
	}

	/**
	 * 抛出实体不存在异常
	 * 
	 * @param msg 不能为null，以键值组成，格式例如：[username: sysadmin]
	 * @author: 黄国庆 2010-12-29 下午03:25:35
	 * @throws EntityExistException
	 */
	protected void throwEntityNotExistException(String msg) throws EntityExistException {
		throw new EntityExistException("(" + getEntityClass().getName() + ") " + msg + "实体不存在！");
	}

	/**
	 * 抛出实体已存在异常
	 * 
	 * @param msg 不能为null，以键值组成，格式例如：[id: sysadmin]
	 * @author: 黄国庆 2010-12-29 下午03:25:35
	 * @throws EntityExistException
	 */
	protected void throwEntityExistException(String msg) throws EntityExistException {
		throw new EntityExistException("(" + getEntityClass().getName() + ") " + msg + "实体已存在！");
	}
}
