
package org.webframe.support.startup;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.webframe.support.driver.loader.DefaultModulePluginLoader;
import org.webframe.support.driver.loader.ModulePluginLoader;
import org.webframe.support.startup.chain.StartupChain;
import org.webframe.support.startup.chain.commands.ApplicationContextSupportCommand;
import org.webframe.support.startup.chain.commands.LoadModulePluginCommand;

/**
 * webframe框架启动类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-7 上午9:06:59
 * @version
 */
public class Startup {

	private String[]													locations			= new String[]{
				"classpath:/startup/wf-startup.xml",
				"classpath*:/startup/wf-chain-*.xml"									};

	private Class<? extends AbstractApplicationContext>	contextClass;

	private Class<? extends ModulePluginLoader>				pluginLoaderClass	= DefaultModulePluginLoader.class;

	private Context													chainContext		= null;

	public Startup(Class<? extends AbstractApplicationContext> contextClass,
				Class<? extends ModulePluginLoader> pluginLoaderClass) {
		this.contextClass = contextClass;
		this.pluginLoaderClass = pluginLoaderClass;
	}

	public void startup() throws Exception {
		initChainContext();
		ClassPathXmlApplicationContext cpxa = new ClassPathXmlApplicationContext(locations);
		StartupChain startupChain = (StartupChain) cpxa.getBean("startupChain");
		ChainBase chainBase = new ChainBase(startupChain.getAllCommands());
		chainBase.execute(chainContext);
	}

	@SuppressWarnings("unchecked")
	protected void initChainContext() {
		if (chainContext == null) {
			chainContext = new ContextBase();
			// context中保存模块插件加载类名称
			chainContext.put(LoadModulePluginCommand.MODULE_PLUGIN_DRIVER_LOADER,
				pluginLoaderClass);
			// 指定command中使用的spring contextClass
			chainContext.put(
				ApplicationContextSupportCommand.SPRING_CONTEXT_CLASS, contextClass);
		}
	}

	/**
	 * 获取chain链上下文
	 * 
	 * @return
	 * @author 黄国庆 2012-6-14 下午4:08:45
	 */
	public Context getChainContext() {
		return chainContext;
	}

	/**
	 * 获取chain链上下文中的spring ApplicationContext，该spring上下文为最后一个命令生成的上下文
	 * 
	 * @return
	 * @author 黄国庆 2012-6-14 下午4:08:58
	 */
	public ApplicationContext getApplicationContext() {
		return (ApplicationContext) chainContext.get(ApplicationContextSupportCommand.SPRING_CONTEXT_CURRENT);
	}
}
