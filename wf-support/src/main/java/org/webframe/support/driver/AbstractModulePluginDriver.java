
package org.webframe.support.driver;

/**
 * 提供模块插件实现类的默认位置配置标准，例如：模块插件实现类org.webframe.core.CoreModulePluginDriver，
 * 我们的Test模块的包路径为org.webframe.test，该包中直接包括model Bean和freemarker模板，
 * 那么CoreModulePluginDriver实现AbstractModulePluginDriver抽象类后，就不需要重新任何方法，只需添加静态块
 * 注册该CoreModulePluginDriver实例
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-19 下午05:59:37
 */
public abstract class AbstractModulePluginDriver implements ModulePluginDriver {

	@Override
	public String getEntityLocation() {
		return "*";
	}

	@Override
	public String getViewTempletLocation() {
		return "";
	}

	@Override
	public String getWebSourcesLocation() {
		return null;
	}

	@Override
	public String getSpringContextLocation() {
		return null;
	}

	@Override
	public String toString() {
		return getModuleName() + "模块";
	}
}
