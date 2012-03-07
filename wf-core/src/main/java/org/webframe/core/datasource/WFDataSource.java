
package org.webframe.core.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.webframe.core.datasource.hsqldb.HsqldbDataSourceUtil;
import org.webframe.core.exception.datasource.ConnectionPoolNotExistException;
import org.webframe.core.exception.datasource.DataBaseNotExistException;
import org.webframe.core.util.BeanUtils;
import org.webframe.core.util.PropertyConfigurerUtils;
import org.webframe.core.util.SqlScriptsUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 通过配置提供 mysql, oracle, sqlserver datasource, 并通过配置使用c3p0或dbcp连接池
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午10:14:27
 */
public class WFDataSource implements DataSource, InitializingBean {

	protected Log						log					= LogFactory.getLog(getClass());

	private static final String	DATASOURCECLASS	= ".initialDataSourceClass";

	private static final String	DIALECT				= ".dialect";

	private static final String	JDBC					= ".jdbc";

	// 默认连接池类型
	private PoolType					poolType				= PoolType.未知连接池;

	// 默认数据库类型
	private DataBaseType				databaseType		= DataBaseType.未知数据库;

	private boolean					defaulted			= false;

	// 数据库Hibernate方言
	private String						dialect				= "";

	// 数据源
	private DataSource				dataSource			= null;

	private String						encoding				= Charset.defaultCharset().name();

	public WFDataSource(DataSource dataSource, String poolType) {
		this.dataSource = dataSource;
		if (poolType != null) {
			this.setPoolType(poolType);
		}
	}

	public WFDataSource(String databaseType, String poolType) throws Exception {
		if (poolType != null) {
			this.setPoolType(poolType);
		}
		if (databaseType != null) {
			this.setDatabaseType(databaseType);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.dataSource == null) {
			this.init();
		}
		if (isDefaulted()) {
			DataSourceUtil.defaultDataSource(this);
			if (PropertyConfigurerUtils.getBoolean("sql.init")) {
				this.initSqlScript();
			}
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (dataSource == null) {
			return null;
		}
		return dataSource.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		if (this.dataSource == null) {
			return null;
		}
		return dataSource.getConnection(username, password);
	}

	public synchronized void close() throws SQLException {
		if (this.dataSource instanceof ComboPooledDataSource) {
			((ComboPooledDataSource) dataSource).close();
		} else if (this.dataSource instanceof BasicDataSource) {
			((BasicDataSource) dataSource).close();
		} else {
			// TODO 非c3p0, dbcp 链接迟时，需要修改
		}
	}

	public PoolType getPoolType() {
		return poolType;
	}

	public void setPoolType(String poolType) {
		checkPoolType(poolType);
		this.poolType = PoolType.getName(poolType);
	}

	public String getDialect() {
		return dialect;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = DataBaseType.getName(databaseType);
		checkDataBaseType(databaseType);
	}

	public DataBaseType getDatabaseType() {
		return databaseType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public final boolean isDefaulted() {
		return defaulted;
	}

	public void setDefaulted(boolean defaulted) {
		this.defaulted = defaulted;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (this.dataSource == null) {
			return null;
		}
		return this.dataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.dataSource != null && this.dataSource.isWrapperFor(iface);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		if (this.dataSource == null) {
			return null;
		}
		return this.dataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		if (this.dataSource != null) {
			this.dataSource.setLogWriter(out);
		}
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		if (this.dataSource != null) {
			this.dataSource.setLoginTimeout(seconds);
		}
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		if (this.dataSource == null) {
			return 0;
		}
		return this.dataSource.getLoginTimeout();
	}

	/**
	 * 初始化数据源
	 * 
	 * @throws Exception
	 * @author: 黄国庆 2010-4-21 下午04:38:43
	 */
	public void init() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("WFDataSource 数据源开始初始化！");
		}
		this.dialect = PropertyConfigurerUtils.getString(this.getDatabaseType().getValue() + DIALECT);
		// 数据库链接properties
		Properties connectionProperties = PropertyConfigurerUtils.getProperties(this.getDatabaseType().getValue() + JDBC);
		if (connectionProperties.isEmpty()) {
			throw new DataBaseNotExistException(this.getDatabaseType().getValue());
		}
		if (DataBaseType.HSQLDB.equals(getDatabaseType())) {
			this.dataSource = HsqldbDataSourceUtil.getHsqldbDataSource(connectionProperties);
			return;
		}
		// 连接池properties
		Properties prosPool = PropertyConfigurerUtils.getProperties(this.getPoolType().getValue());
		if (prosPool == null) {
			throw new ConnectionPoolNotExistException(this.getPoolType().getValue());
		}
		// 处理连接池属性
		disposePoolProperties(connectionProperties);
		prosPool.putAll(connectionProperties);
		// 获取连接池的class路径
		String initialDataSourceClass = PropertyConfigurerUtils.getString(this.getPoolType().getValue() + DATASOURCECLASS);
		if (initialDataSourceClass == null) {
			throw new Exception("连接池实现类没有配置！");
		}
		Class<?> clazz = Class.forName(initialDataSourceClass);
		this.dataSource = (DataSource) clazz.newInstance();
		// 对datasource进行相关参数初始化
		BeanUtils.setBeanProperties(this.dataSource, prosPool);
	}

	/**
	 * 初始化sql脚本
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @author: 黄国庆 2011-1-17 下午02:57:46
	 */
	private void initSqlScript() throws SQLException, IOException {
		SqlScriptsUtils.modulesSqlScriptsInit(getDatabaseType(), this);
	}

	private void checkPoolType(String poolType) {
		if (PoolType.getName(poolType).equals(PoolType.未知连接池)) {
			throw new UnsupportedOperationException("不支持该类型的连接池！---->" + poolType);
		}
	}

	private void checkDataBaseType(String databaseType) {
		if (DataBaseType.getName(databaseType).equals(DataBaseType.未知数据库)) {
			throw new UnsupportedOperationException("不支持该类型的数据库！---->" + databaseType);
		}
	}

	private void disposePoolProperties(Properties connectionProperties) {
		if (PoolType.C3P0.equals(getPoolType())) {
			connectionProperties.put("driverClass", connectionProperties.get("driverClassName"));
			connectionProperties.put("jdbcUrl", connectionProperties.get("url"));
			connectionProperties.put("user", connectionProperties.get("username"));
		}
	}
}
