package org.webframe.support.startup.chain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 框架启动执行命令目录，
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-7 上午8:53:06
 * @version
 */
public class StartupChainCatalog implements Catalog, ApplicationContextAware {

	private ApplicationContext	applicationContext	= null;

	private List<Command>		beforeLoadModulePlugin;

	private List<Command>		afterLoadModulePlugin;

	private List<Command>		beforeConnectDataBase;

	private List<Command>		afterConnectDataBase;

	private List<Command>		beforeLoadHibernate;

	private List<Command>		afterLoadHibernate;

	private List<Command>		beforeLoadSpring;

	private List<Command>		afterLoadSpring;

	private List<Command>		beforeLoadValueList;

	private List<Command>		afterLoadValueList;

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
				throws BeansException {
		this.applicationContext = applicationContext;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Catalog#addCommand(java.lang.String, org.apache.commons.chain.Command)
	 */
	@Override
	public void addCommand(String name, Command command) {
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Catalog#getCommand(java.lang.String)
	 */
	@Override
	public Command getCommand(String name) {
		return (Command) getApplicationContext().getBean(name);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Catalog#getNames()
	 */
	@Override
	public Iterator<?> getNames() {
		Map<String, Command> commands = null;
		try {
			commands = getApplicationContext().getBeansOfType(Command.class, true,
				true);
		} catch (Exception e) {
			commands = new HashMap<String, Command>();
			System.err.println("Error in retrieving command names..");
		}
		return commands.keySet().iterator();
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setBeforeLoadModulePlugin(List<Command> beforeLoadModulePlugin) {
		this.beforeLoadModulePlugin = beforeLoadModulePlugin;
	}

	public List<Command> getBeforeLoadModulePlugin() {
		if (beforeLoadModulePlugin == null) {
			return Collections.emptyList();
		}
		return beforeLoadModulePlugin;
	}

	public void setAfterLoadModulePlugin(List<Command> afterLoadModulePlugin) {
		this.afterLoadModulePlugin = afterLoadModulePlugin;
	}

	public List<Command> getAfterLoadModulePlugin() {
		if (afterLoadModulePlugin == null) {
			return Collections.emptyList();
		}
		return afterLoadModulePlugin;
	}

	public void setBeforeConnectDataBase(List<Command> beforeConnectDataBase) {
		this.beforeConnectDataBase = beforeConnectDataBase;
	}

	public List<Command> getBeforeConnectDataBase() {
		if (beforeConnectDataBase == null) {
			return Collections.emptyList();
		}
		return beforeConnectDataBase;
	}

	public void setAfterConnectDataBase(List<Command> afterConnectDataBase) {
		this.afterConnectDataBase = afterConnectDataBase;
	}

	public List<Command> getAfterConnectDataBase() {
		if (afterConnectDataBase == null) {
			return Collections.emptyList();
		}
		return afterConnectDataBase;
	}

	public void setBeforeLoadHibernate(List<Command> beforeLoadHibernate) {
		this.beforeLoadHibernate = beforeLoadHibernate;
	}

	public List<Command> getBeforeLoadHibernate() {
		if (beforeLoadHibernate == null) {
			return Collections.emptyList();
		}
		return beforeLoadHibernate;
	}

	public void setAfterLoadHibernate(List<Command> afterLoadHibernate) {
		this.afterLoadHibernate = afterLoadHibernate;
	}

	public List<Command> getAfterLoadHibernate() {
		if (afterLoadHibernate == null) {
			return Collections.emptyList();
		}
		return afterLoadHibernate;
	}

	public void setBeforeLoadSpring(List<Command> beforeLoadSpring) {
		this.beforeLoadSpring = beforeLoadSpring;
	}

	public List<Command> getBeforeLoadSpring() {
		if (beforeLoadSpring == null) {
			return Collections.emptyList();
		}
		return beforeLoadSpring;
	}

	public void setAfterLoadSpring(List<Command> afterLoadSpring) {
		this.afterLoadSpring = afterLoadSpring;
	}

	public List<Command> getAfterLoadSpring() {
		if (afterLoadSpring == null) {
			return Collections.emptyList();
		}
		return afterLoadSpring;
	}

	public void setBeforeLoadValueList(List<Command> beforeLoadValueList) {
		this.beforeLoadValueList = beforeLoadValueList;
	}

	public List<Command> getBeforeLoadValueList() {
		if (beforeLoadValueList == null) {
			return Collections.emptyList();
		}
		return beforeLoadValueList;
	}

	public void setAfterLoadValueList(List<Command> afterLoadValueList) {
		this.afterLoadValueList = afterLoadValueList;
	}

	public List<Command> getAfterLoadValueList() {
		if (afterLoadValueList == null) {
			return Collections.emptyList();
		}
		return afterLoadValueList;
	}
}