
package org.webframe.core.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.webframe.core.datasource.WFDataSource;

public class WFSessionFactoryBean extends AnnotationSessionFactoryBean {

	private Map<String, LobHandler>	lobHandlers					= new HashMap<String, LobHandler>();

	private static final String		defaultLobHandlerString	= "default";

	private static final String		HIBERNATE_DIALECT			= "hibernate.dialect";

	private TypeFilter[]					entityTypeFilters;

	public TypeFilter[] getEntityTypeFilters() {
		return entityTypeFilters;
	}

	public void setEntityTypeFilters(TypeFilter[] entityTypeFilters) {
		this.entityTypeFilters = entityTypeFilters;
	}

	public Map<String, LobHandler> getLobHandlers() {
		return lobHandlers;
	}

	public void setLobHandlers(Map<String, LobHandler> lobHandlers) {
		this.lobHandlers = lobHandlers;
	}

	@Override
	protected SessionFactory wrapSessionFactoryIfNecessary(SessionFactory rawSf) {
		return new WFSessionFactoryWrapper(rawSf, this);
	}

	protected SessionFactory buildSessionFactory() throws Exception {
		if (hasWFDataSource()) {
			// 设置lobhandler
			WFDataSource bds = (WFDataSource) this.getDataSource();
			String databaseType = bds.getDatabaseType().getValue();
			LobHandler lobHandler = getLobHandlers().get(databaseType);
			if (lobHandler == null) {
				lobHandler = getLobHandlers().get(defaultLobHandlerString);
			}
			super.setLobHandler(lobHandler);
			// 设置dialect
			if (bds.getDialect() != null) {
				this.getHibernateProperties().put(HIBERNATE_DIALECT, bds.getDialect());
			}
		}
		return super.buildSessionFactory();
	}

	protected boolean hasWFDataSource() {
		return this.getDataSource() instanceof WFDataSource;
	}

	@Override
	protected void postProcessMappings(Configuration config) throws HibernateException {
		super.postProcessMappings(config);
		// 加载jar包中的hbm配置文件
		HibernateEntityUtils.addJarHbm(config);
		// 加载模块中的注解Entity
		HibernateEntityUtils.addAnnotatedClass((AnnotationConfiguration) config, entityTypeFilters);
	}
}
