package org.webframe.struts;

import org.webframe.core.sql.ISqlScriptSupport;


/**
 * 指定默认struts和sql脚本根目录文件夹，供有提供struts配置和sql脚本的模块使用
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-13 下午8:24:59
 * @version
 */
public abstract class AbstractStrutsAndSqlModulePluginDriver
			extends
				AbstractStrutsModulePluginDriver implements ISqlScriptSupport {


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
