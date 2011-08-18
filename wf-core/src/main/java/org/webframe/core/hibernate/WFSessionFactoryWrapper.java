
package org.webframe.core.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.webframe.core.datasource.DataBaseType;
import org.webframe.core.datasource.WFDataSource;

/**
 * Hibernate SessionFactory 包装类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: SessionFactoryWrapper.java,v 1.1.2.4 2010/04/23 09:13:54 huangguoqing Exp $ Create:
 *          2010-4-22 下午03:22:45
 */
@SuppressWarnings("rawtypes")
public class WFSessionFactoryWrapper implements SessionFactory {

	/**
	 * 
	 */
	private static final long		serialVersionUID		= 1324814669169343743L;

	private static final Log		log						= LogFactory.getLog(WFSessionFactoryWrapper.class);

	private SessionFactory			sessionFactory			= null;

	private WFSessionFactoryBean	wFSessionFactoryBean	= null;

	private DataBaseType				databaseType			= DataBaseType.未知数据库;

	protected WFSessionFactoryWrapper(SessionFactory sessionFactory, WFSessionFactoryBean wFSessionFactoryBean) {
		this.sessionFactory = sessionFactory;
		this.wFSessionFactoryBean = wFSessionFactoryBean;
		refreshDatabaseType();
		EntityUtil.init(this.sessionFactory.getAllClassMetadata());
	}

	protected void refreshSessionFactory() throws Exception {
		if (log.isInfoEnabled()) {
			String olddb = getDatabaseType().getValue();
			refreshDatabaseType();
			String newdb = getDatabaseType().getValue();
			if (olddb.equals(newdb)) {
				log.info("当前数据库为 " + newdb + " 数据库, 正在刷新 SessionFactory ！");
			} else {
				log.info("当前数据库为 " + olddb + " 数据库，正在切换至 " + newdb + " 数据库！");
			}
		}
		refreshDatabaseType();
		if (this.sessionFactory != null) {
			this.sessionFactory.close();
		}
		this.sessionFactory = this.wFSessionFactoryBean.buildSessionFactory();
		EntityUtil.init(this.sessionFactory.getAllClassMetadata());
	}

	private void refreshDatabaseType() {
		if (this.wFSessionFactoryBean.hasWFDataSource()) {
			WFDataSource bds = (WFDataSource) this.wFSessionFactoryBean.getDataSource();
			this.databaseType = bds.getDatabaseType();
		}
	}

	public DataBaseType getDatabaseType() {
		return this.databaseType;
	}

	@Override
	public void close() throws HibernateException {
		this.sessionFactory.close();
	}

	@Override
	public void evict(Class persistentClass) throws HibernateException {
		this.sessionFactory.evict(persistentClass);
	}

	@Override
	public void evict(Class persistentClass, Serializable id) throws HibernateException {
		this.sessionFactory.evict(persistentClass, id);
	}

	@Override
	public void evictCollection(String roleName) throws HibernateException {
		this.sessionFactory.evictCollection(roleName);
	}

	@Override
	public void evictCollection(String roleName, Serializable id) throws HibernateException {
		this.sessionFactory.evictCollection(roleName, id);
	}

	@Override
	public void evictEntity(String entityName) throws HibernateException {
		this.sessionFactory.evictEntity(entityName);
	}

	@Override
	public void evictEntity(String entityName, Serializable id) throws HibernateException {
		this.sessionFactory.evictEntity(entityName, id);
	}

	@Override
	public void evictQueries() throws HibernateException {
		this.sessionFactory.evictQueries();
	}

	@Override
	public void evictQueries(String cacheRegion) throws HibernateException {
		this.sessionFactory.evictQueries(cacheRegion);
	}

	@Override
	public Map<?, ?> getAllClassMetadata() throws HibernateException {
		return this.sessionFactory.getAllClassMetadata();
	}

	@Override
	public Map<?, ?> getAllCollectionMetadata() throws HibernateException {
		return this.sessionFactory.getAllCollectionMetadata();
	}

	@Override
	public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException {
		return this.sessionFactory.getClassMetadata(persistentClass);
	}

	@Override
	public ClassMetadata getClassMetadata(String entityName) throws HibernateException {
		return this.sessionFactory.getClassMetadata(entityName);
	}

	@Override
	public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException {
		return this.sessionFactory.getCollectionMetadata(roleName);
	}

	@Override
	public Session getCurrentSession() throws HibernateException {
		return (Session) SessionFactoryUtils.getSession(this, true);
	}

	@Override
	public Set<?> getDefinedFilterNames() {
		return this.sessionFactory.getDefinedFilterNames();
	}

	@Override
	public FilterDefinition getFilterDefinition(String filterName) throws HibernateException {
		return this.sessionFactory.getFilterDefinition(filterName);
	}

	@Override
	public Statistics getStatistics() {
		return this.sessionFactory.getStatistics();
	}

	@Override
	public boolean isClosed() {
		return this.sessionFactory.isClosed();
	}

	@Override
	public Session openSession() throws HibernateException {
		return this.sessionFactory.openSession();
	}

	@Override
	public Session openSession(Connection connection) {
		return this.sessionFactory.openSession(connection);
	}

	@Override
	public Session openSession(Interceptor interceptor) throws HibernateException {
		return this.sessionFactory.openSession(interceptor);
	}

	@Override
	public Session openSession(Connection connection, Interceptor interceptor) {
		return this.sessionFactory.openSession(connection, interceptor);
	}

	@Override
	public StatelessSession openStatelessSession() {
		return this.sessionFactory.openStatelessSession();
	}

	@Override
	public StatelessSession openStatelessSession(Connection connection) {
		return this.sessionFactory.openStatelessSession(connection);
	}

	@Override
	public Reference getReference() throws NamingException {
		return this.sessionFactory.getReference();
	}
}
