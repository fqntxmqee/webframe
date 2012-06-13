package org.webframe.core.sql;

import org.webframe.support.driver.AbstractModulePluginDriver;


/**
 * 指定默认sql脚本根目录位置的抽象模块驱动，供有sql脚本初始化的模块使用
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-10 下午9:05:03
 * @version
 */
public abstract class AbstractSqlModulePluginDriver
			extends
				AbstractModulePluginDriver implements ISqlScriptSupport {


	/**
	 * 指定默认sql脚本根目录位置："/sql"
	 * 
	 * @see org.webframe.core.sql.ISqlScriptSupport#getSqlScriptLocation()
	 */
	@Override
	public String getSqlScriptLocation() {
		return "/sql";
	}
}
