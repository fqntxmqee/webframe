
package org.webframe.core.service;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webframe.core.dao.IBaseDao;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.exception.entity.EntityNotExistException;
import org.webframe.core.exception.entity.EntityNullException;
import org.webframe.core.model.BaseEntity;

/**
 * 类功能描述：Service基类实现类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-21 下午09:34:17
 */
@Service
public class BaseService implements IBaseService {

	protected Log		log	= LogFactory.getLog(getClass());

	@Autowired
	private IBaseDao	baseDao;

	protected IBaseDao getBaseDao() {
		return baseDao;
	}

	@Override
	public void save(BaseEntity obj) throws ServiceException {
		entityNullValidate(obj);
		getBaseDao().save(obj);
	}

	@Override
	public void delete(BaseEntity obj) throws ServiceException {
		entityNullValidate(obj);
		getBaseDao().delete(obj);
	}

	@Override
	public void delete(Class<?> clazz, Serializable id) throws ServiceException {
		if (id == null) throw new ServiceException("待删除的业务对象主键ID不能为null！");
		Object obj = get(clazz, id);
		if (obj == null) throw new EntityNotExistException("指定业务对象("
					+ clazz.getSimpleName()
					+ ")主键ID("
					+ id
					+ ")数据记录不存在！");
		getBaseDao().delete(obj);
	}

	@Override
	public void saveOrUpdate(BaseEntity obj) throws ServiceException {
		entityNullValidate(obj);
		getBaseDao().saveOrUpdate(obj);
	}

	@Override
	public void update(BaseEntity obj) throws ServiceException {
		entityNullValidate(obj);
		getBaseDao().update(obj);
	}

	@Override
	public Object get(Class<?> clazz, Serializable id) {
		return getBaseDao().get(clazz, id);
	}

	@Override
	public Object load(Class<?> clazz, Serializable id) {
		return getBaseDao().load(clazz, id);
	}

	/**
	 * @function 如果entity为null，抛出异常EntityNullException
	 * @param obj entity
	 * @author 黄国庆 2011-3-23 下午08:32:06
	 */
	protected void entityNullValidate(BaseEntity obj) throws EntityNullException {
		if (obj == null) throw new EntityNullException();
	}
}
