
package org.webframe.support.driver;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarFile;

import org.webframe.support.driver.loader.ModulePluginLoader;
import org.webframe.support.driver.resource.jar.JarResourceLoader;
import org.webframe.support.util.ClassUtils;
import org.webframe.support.util.SystemLogUtils;

import sun.misc.Service;
import sun.security.action.GetPropertyAction;

/**
 * webframe框架模块插件管理器，实现ModulePluginDriver接口类的实现类，
 * 需要在实现类的静态快中注册自己的实例"ModulePluginManager.registerDriver(ModulePluginDriver driver)"
 * 
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-18 下午06:46:28
 */
public final class ModulePluginManager {

	private static boolean									initialized		= false;

	/* write copy of the drivers vector */
	private static Vector<ModulePluginDriverInfo>	writeDrivers	= new Vector<ModulePluginDriverInfo>();

	/* write copy of the drivers vector */
	private static Vector<ModulePluginDriverInfo>	readDrivers		= new Vector<ModulePluginDriverInfo>();

	private static native ClassLoader getCallerClassLoader();

	/**
	 * 注册模块插件驱动
	 * 
	 * @param driver 模块插件驱动实现类
	 * @author 黄国庆 2011-4-2 上午11:21:38
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void registerDriver(ModulePluginDriver driver) {
		if (!initialized) {
			initialize();
		}
		ModulePluginDriverInfo mpdi = initDriverInfo(driver);
		// Not Required -- drivers.addElement(di);
		writeDrivers.addElement(mpdi);
		SystemLogUtils.println("registerDriver: " + mpdi + "(" + writeDrivers.size() + ")");
		/* update the read copy of drivers vector */
		readDrivers = (Vector<ModulePluginDriverInfo>) writeDrivers.clone();
	}

	public static Enumeration<ModulePluginDriver> getDrivers() {
		Vector<ModulePluginDriver> result = new Vector<ModulePluginDriver>();
		Vector<ModulePluginDriverInfo> drivers = null;
		if (!initialized) {
			initialize();
		}
		synchronized (ModulePluginManager.class) {
			// use the readcopy of drivers
			drivers = readDrivers;
		}
		for (int i = 0; i < drivers.size(); i++) {
			result.addElement(drivers.elementAt(i).driver);
		}
		return result.elements();
	}

	static Enumeration<ModulePluginDriverInfo> getDriverInfos() {
		Vector<ModulePluginDriverInfo> drivers = null;
		if (!initialized) {
			initialize();
		}
		synchronized (ModulePluginManager.class) {
			// use the readcopy of drivers
			drivers = readDrivers;
		}
		return drivers.elements();
	}

	static void initialize() {
		if (initialized) {
			return;
		}
		initialized = true;
		loadInitialDrivers();
		SystemLogUtils.println("ModulePluginManager initialized");
	}

	@SuppressWarnings("unchecked")
	private static ModulePluginDriverInfo initDriverInfo(ModulePluginDriver driver) {
		ModulePluginDriverInfo mpdi = new ModulePluginDriverInfo();
		mpdi.driver = driver;
		mpdi.driverClass = (Class<ModulePluginDriver>) driver.getClass();
		mpdi.driverClassName = mpdi.driverClass.getSimpleName();
		if (ClassUtils.isInJar(mpdi.driverClass)) {
			try {
				mpdi.jarResourceLoader = new JarResourceLoader(mpdi.driverClass);
				JarFile jarFile = mpdi.jarResourceLoader.getJarURLConnection().getJarFile();
				mpdi.lastModifyTime = mpdi.jarResourceLoader.getJarURLConnection().getLastModified();
				String name = jarFile.getName();
				jarFile.close();
				mpdi.jarName = name.substring(name.lastIndexOf(File.separator) + 1);
				mpdi.inJar = true;
			} catch (Exception e) {
				SystemLogUtils.errorPrintln(driver + "不存在jar！");
			}
		}
		return mpdi;
	}

	private static void loadInitialDrivers() {
		String drivers;
		try {
			// 由系统变量获取drivers，如果未定义，则不加载。
			String systemVar = ModulePluginLoader.MODULE_PLUGIN_KEY;
			drivers = (String) AccessController.doPrivileged(new GetPropertyAction(systemVar));
		} catch (Exception ex) {
			drivers = null;
		}
		// If the driver is packaged as a Service Provider,
		// load it.
		// Get all the drivers through the classloader
		// exposed as a java.sql.Driver.class service.
		DriverService ds = new DriverService();
		// Have all the privileges to get all the
		// implementation of java.sql.Driver
		AccessController.doPrivileged(ds);
		SystemLogUtils.println("ModulePluginManager.initialize: modulePlugin.drivers = " + drivers);
		if (drivers == null) {
			return;
		}
		while (drivers.length() != 0) {
			int x = drivers.indexOf(':');
			String driver;
			if (x < 0) {
				driver = drivers;
				drivers = "";
			} else {
				driver = drivers.substring(0, x);
				drivers = drivers.substring(x + 1);
			}
			if (driver.length() == 0) {
				continue;
			}
			try {
				SystemLogUtils.println("ModulePluginManager.initialize: loading " + driver);
				Class.forName(driver);
			} catch (Exception ex) {
				SystemLogUtils.println("ModulePluginManager.initialize: load failed: " + ex);
			}
		}
	}
}

class DriverService implements PrivilegedAction<ModulePluginDriver> {

	Iterator<ModulePluginDriver>	ps	= null;

	public DriverService() {
	}

	@SuppressWarnings("unchecked")
	public ModulePluginDriver run() {
		ps = Service.providers(ModulePluginDriver.class);
		try {
			while (ps.hasNext()) {
				ps.next();
			}
		} catch (Throwable t) {
		}
		return null;
	}
}