/*
 * wf-core
 * Created on 2012-2-6-下午09:18:15
 */

package org.webframe.core.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

/**
 * NullSessionFactory，不初始化
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-6 下午09:18:15
 * @version
 */
@SuppressWarnings("rawtypes")
public class NullSessionFactory implements SessionFactory {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -867477322382703596L;

	/* (non-Javadoc)
	 * @see javax.naming.Referenceable#getReference()
	 */
	@Override
	public Reference getReference() throws NamingException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#openSession(java.sql.Connection)
	 */
	@Override
	public Session openSession(Connection connection) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#openSession(org.hibernate.Interceptor)
	 */
	@Override
	public Session openSession(Interceptor interceptor) throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#openSession(java.sql.Connection, org.hibernate.Interceptor)
	 */
	@Override
	public Session openSession(Connection connection, Interceptor interceptor) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#openSession()
	 */
	@Override
	public Session openSession() throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getCurrentSession()
	 */
	@Override
	public Session getCurrentSession() throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.Class)
	 */
	@Override
	public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.String)
	 */
	@Override
	public ClassMetadata getClassMetadata(String entityName) throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getCollectionMetadata(java.lang.String)
	 */
	@Override
	public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getAllClassMetadata()
	 */
	@Override
	public Map<?, ?> getAllClassMetadata() throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getAllCollectionMetadata()
	 */
	@Override
	public Map<?, ?> getAllCollectionMetadata() throws HibernateException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getStatistics()
	 */
	@Override
	public Statistics getStatistics() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#close()
	 */
	@Override
	public void close() throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evict(java.lang.Class)
	 */
	@Override
	public void evict(Class persistentClass) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evict(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public void evict(Class persistentClass, Serializable id) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String)
	 */
	@Override
	public void evictEntity(String entityName) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String, java.io.Serializable)
	 */
	@Override
	public void evictEntity(String entityName, Serializable id) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evictCollection(java.lang.String)
	 */
	@Override
	public void evictCollection(String roleName) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evictCollection(java.lang.String, java.io.Serializable)
	 */
	@Override
	public void evictCollection(String roleName, Serializable id) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evictQueries()
	 */
	@Override
	public void evictQueries() throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#evictQueries(java.lang.String)
	 */
	@Override
	public void evictQueries(String cacheRegion) throws HibernateException {
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#openStatelessSession()
	 */
	@Override
	public StatelessSession openStatelessSession() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#openStatelessSession(java.sql.Connection)
	 */
	@Override
	public StatelessSession openStatelessSession(Connection connection) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getDefinedFilterNames()
	 */
	@Override
	public Set<?> getDefinedFilterNames() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.SessionFactory#getFilterDefinition(java.lang.String)
	 */
	@Override
	public FilterDefinition getFilterDefinition(String filterName) throws HibernateException {
		return null;
	}
}
