
package org.webframe.support.startup.chain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.chain.Command;
import org.springframework.beans.factory.InitializingBean;
import org.webframe.support.startup.chain.commands.LoadModulePluginCommand;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-7 上午9:18:32
 * @version
 */
public class StartupChain implements InitializingBean {

	private final Command			loadModulePlugin	= new LoadModulePluginCommand();

	private Command					connectDataBase	= null;

	private Command					loadHibernate		= null;

	private Command					loadSpring			= null;

	private Command					loadValueList		= null;

	private final List<Command>	allConmands			= new ArrayList<Command>(16);

	private StartupChainCatalog	startupChainCatalog;

	@Override
	public void afterPropertiesSet() throws Exception {
		// loadModulePlugin
		allConmands.addAll(startupChainCatalog.getBeforeLoadModulePlugin());
		allConmands.add(loadModulePlugin);
		allConmands.addAll(startupChainCatalog.getAfterLoadModulePlugin());
		// connectDataBase
		allConmands.addAll(startupChainCatalog.getBeforeConnectDataBase());
		if (connectDataBase != null) {
			allConmands.add(connectDataBase);
		}
		allConmands.addAll(startupChainCatalog.getAfterConnectDataBase());
		// loadHibernate
		allConmands.addAll(startupChainCatalog.getBeforeLoadHibernate());
		if (loadHibernate != null) {
			allConmands.add(loadHibernate);
		}
		allConmands.addAll(startupChainCatalog.getAfterLoadHibernate());
		// loadSpring
		allConmands.addAll(startupChainCatalog.getBeforeLoadSpring());
		if (loadSpring != null) {
			allConmands.add(loadSpring);
		}
		allConmands.addAll(startupChainCatalog.getAfterLoadSpring());
		// loadValueList
		allConmands.addAll(startupChainCatalog.getBeforeLoadValueList());
		if (loadValueList != null) {
			allConmands.add(loadValueList);
		}
		allConmands.addAll(startupChainCatalog.getAfterLoadValueList());
	}

	public void setStartupChainCatalog(StartupChainCatalog startupChainCatalog) {
		this.startupChainCatalog = startupChainCatalog;
	}

	public StartupChainCatalog getStartupChainCatalog() {
		return startupChainCatalog;
	}

	public void setConnectDataBase(Command connectDataBase) {
		this.connectDataBase = connectDataBase;
	}

	public void setLoadHibernate(Command loadHibernate) {
		this.loadHibernate = loadHibernate;
	}

	public void setLoadSpring(Command loadSpring) {
		this.loadSpring = loadSpring;
	}

	public void setLoadValueList(Command loadValueList) {
		this.loadValueList = loadValueList;
	}

	public List<Command> getAllCommands() {
		return allConmands;
	}
}
