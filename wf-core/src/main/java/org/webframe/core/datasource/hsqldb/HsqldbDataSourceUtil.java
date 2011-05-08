
package org.webframe.core.datasource.hsqldb;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.webframe.core.util.BeanUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-17
 *          下午02:46:28
 */
public abstract class HsqldbDataSourceUtil {

	public static DataSource getHsqldbDataSource(Properties connectionProperties) {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		BeanUtils.setBeanProperties(ds, connectionProperties);
		return ds;
	}
}
