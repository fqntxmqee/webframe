/*
 * wf-core
 * Created on 2011-5-8-下午04:15:21
 */

package org.webframe.core.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.core.io.Resource;
import org.webframe.core.datasource.DataSourceUtil;

/**
 * 数据源工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-8 下午04:15:21
 */
public class DataSourceUtils extends DataSourceUtil {

	public static void executeSqlScripts(Resource resource, DataSource ds) throws SQLException, IOException {
		Reader reader = new InputStreamReader(resource.getInputStream());
		executeSqlScripts(reader, ds);
	}
}
