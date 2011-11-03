/*
 * wf-core
 * Created on 2011-10-31-下午05:13:57
 */

package org.webframe.core.util;

import java.io.InputStream;
import java.sql.SQLException;

import org.junit.Test;
import org.webframe.core.datasource.DataBaseType;
import org.webframe.core.datasource.DataSourceUtil;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-10-31 下午05:13:57
 */
public class SqlScriptsUtilsTest extends BaseSpringTests {

	/**
	 * Test method for
	 * {@link org.webframe.core.util.SqlScriptsUtils#analyzeSqlFile(java.io.InputStream, java.lang.String)}
	 * .
	 */
	@Test
	public void testAnalyzeSqlFile() {
		InputStream input = this.getClass().getResourceAsStream("/sql-init-mysql-core.sql");
		SqlScriptsUtils.analyzeSqlFile(input, "UTF-8");
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.SqlScriptsUtils#executeBatchSql(java.util.Map, javax.sql.DataSource)}
	 * .
	 */
	@Test
	public void testExecuteBatchSql() throws SQLException {
		InputStream input = this.getClass().getResourceAsStream("/sql-init-" + DataBaseType.HSQLDB + "-core.sql");
		if (DataSourceUtil.getDataBaseType() != DataBaseType.HSQLDB) {
			SqlScriptsUtils.executeBatchSql(SqlScriptsUtils.analyzeSqlFile(input), DataSourceUtil.getDataSource());
		} else {
			SqlScriptsUtils.executeSql(SqlScriptsUtils.analyzeSqlFile(input), DataSourceUtil.getDataSource());
		}
	}
}
