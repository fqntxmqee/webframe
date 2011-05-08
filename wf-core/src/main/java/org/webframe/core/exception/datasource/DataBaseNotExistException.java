
package org.webframe.core.exception.datasource;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: DataBaseNotExistException.java,v 1.1.2.1 2010/04/22 09:12:35 huangguoqing Exp $
 *          Create: 2010-4-20 上午10:05:17
 */
public class DataBaseNotExistException extends DataSourceException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 300116215920133208L;

	public DataBaseNotExistException(String databaseType) {
		super(databaseType + " 类型数据库配置不存在！");
	}
}
