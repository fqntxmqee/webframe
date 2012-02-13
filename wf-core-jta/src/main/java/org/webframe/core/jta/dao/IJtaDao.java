/*
 * wf-core-jta
 * Created on 2011-6-29-下午04:22:10
 */

package org.webframe.core.jta.dao;

import java.util.List;

import org.webframe.core.dao.IBaseDao;
import org.webframe.core.datasource.DataBaseType;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午04:22:10
 */
public interface IJtaDao extends IBaseDao {

	List<String> getAllTables();

	void createTable(String schema, String tableName, String tableContent);

	DataBaseType getJtaDataBaseType();
}
