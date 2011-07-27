
package org.webframe.support.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.driver.exception.ModulePluginConfigException;
import org.webframe.support.driver.resource.jar.JarResourcePatternResolver;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午04:21:54
 */
public abstract class ModulePluginUtils {

	public static final String									DELIMITERS					= ",; \t\n";

	protected static final String								PREFIX_CLASSPATH			= "classpath:";

	private static final String								CONFIG_FILE_NAME			= ".#modulePluginConfig";

	private static Map<String, ModulePluginDriverInfo>	cacheDriverInfoMap		= new HashMap<String, ModulePluginDriverInfo>();

	private static List<ModulePluginDriverInfo>			needUpdateDriverInfos	= new ArrayList<ModulePluginDriverInfo>();

	/**
	 * 缓存已加载的模块插件配置信息到物理文件
	 * 
	 * @param realConfigDirectory 模块插件配置信息缓存文件夹路径，绝对路径
	 * @author 黄国庆 2011-4-30 下午01:39:24
	 */
	public static void cacheModulePluginConfig(String realConfigDirectory) {
		if (realConfigDirectory == null || "".equals(realConfigDirectory)) {
			throw new ModulePluginConfigException("模块插件配置信息缓存文件夹路径不能为空或null！");
		}
		File directory = new File(realConfigDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		} else if (directory.isFile()) {
			throw new ModulePluginConfigException(realConfigDirectory + "不是文件夹路径！");
		}
		try {
			File file = new File(realConfigDirectory, CONFIG_FILE_NAME);
			if (file.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				Object obj = ois.readObject();
				ois.close();
				Map<String, Long> cacheMap = new HashMap<String, Long>(8);
				if (obj instanceof List) {
					List<?> list = (List<?>) obj;
					for (Object object : list) {
						if (object instanceof SerializableDriverInfo) {
							SerializableDriverInfo sdi = (SerializableDriverInfo) object;
							if (sdi.inJar) {
								cacheMap.put(sdi.driverClass, sdi.lastModifyTime);
							}
						}
					}
				}
				Enumeration<ModulePluginDriverInfo> driverInfos = getDriverInfos();
				while (driverInfos.hasMoreElements()) {
					ModulePluginDriverInfo driverInfo = driverInfos.nextElement();
					String key = driverInfo.driverClass.getName();
					if (cacheMap.containsKey(key) && cacheMap.get(key) != driverInfo.lastModifyTime) {
						needUpdateDriverInfos.add(driverInfo);
					}
				}
				if (needUpdateDriverInfos.size() > 0) {
					SystemLogUtils.rootPrintln("更新模块插件驱动详细信息缓存文件Begin！");
					for (ModulePluginDriverInfo mpdi : needUpdateDriverInfos) {
						SystemLogUtils.secondPrintln("更新" + mpdi.getDriver() + "缓存文件！");
					}
					serializeDriverInfos(file, false);
					SystemLogUtils.rootPrintln("更新模块插件驱动详细信息缓存文件End！");
				}
			} else {
				SystemLogUtils.rootPrintln("创建模块插件驱动详细信息缓存文件Begin！");
				serializeDriverInfos(file, true);
				SystemLogUtils.rootPrintln("创建模块插件驱动详细信息缓存文件End！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModulePluginConfigException(realConfigDirectory + "模块插件配置信息缓存文件写入信息失败！");
		}
	}

	protected static List<ModulePluginDriverInfo> getNeedUpdateDriverInfos() {
		return needUpdateDriverInfos;
	}

	public static Enumeration<ModulePluginDriverInfo> getDriverInfos() {
		return ModulePluginManager.getDriverInfos();
	}

	/**
	 * 根据模块驱动类名称，获取模块驱动类详细信息
	 * 
	 * @param driverClassName 模块驱动类名称，字符串（包括包路径）
	 * @return 如果不存在抛出异常
	 * @throws DriverNotExistException
	 * @author 黄国庆 2011-5-1 下午01:40:25
	 */
	public static ModulePluginDriverInfo getDriverInfo(String driverClassName) throws DriverNotExistException {
		if (cacheDriverInfoMap.containsKey(driverClassName)) {
			return cacheDriverInfoMap.get(driverClassName);
		}
		Enumeration<ModulePluginDriverInfo> infos = getDriverInfos();
		while (infos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = infos.nextElement();
			if (driverInfo.getDriverClass().getName().equals(driverClassName)) {
				cacheDriverInfoMap.put(driverClassName, driverInfo);
				return driverInfo;
			}
		}
		throw new DriverNotExistException(driverClassName);
	}

	@SuppressWarnings("unchecked")
	public static <X> Enumeration<X> getDrivers(Class<X> clazz) {
		Enumeration<ModulePluginDriverInfo> infos = getDriverInfos();
		Vector<X> drivers = new Vector<X>();
		while (infos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = infos.nextElement();
			if (clazz != null && clazz.isAssignableFrom(driverInfo.getDriver().getClass())) {
				drivers.add((X) driverInfo.driver);
			}
		}
		return drivers.elements();
	}

	/**
	 * 根据ClassRelativeResourceLoader加载符合pattern的资源
	 * 
	 * @param loaderClass 定位class
	 * @param pattern 匹配符
	 * @return 可能为null
	 * @author 黄国庆 2011-4-6 上午09:46:22
	 */
	protected static Resource[] getResources(ModulePluginDriverInfo driverInfo, String pattern) {
		try {
			JarResourcePatternResolver jarResourcePatternResolver;
			if (driverInfo.isInJar()) {
				jarResourcePatternResolver = new JarResourcePatternResolver(driverInfo.jarResourceLoader);
			} else {
				jarResourcePatternResolver = new JarResourcePatternResolver(new DefaultResourceLoader());
				pattern = PREFIX_CLASSPATH + pattern;
			}
			return jarResourcePatternResolver.getResources(pattern);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 根据指定class处理path，如果path为null，则返回null；如果path以"/"Begin，则直接返回path；
	 * 如果path不以"/"开始，那么返回loaderClass所在包的路径再加上path路径。
	 * 
	 * @param loaderClass 指定class，根据该clas所在的包来定位path的最终路径
	 * @param path 包的相对路径，或者是相对于loaderClass的相对路径
	 * @return 可能为null
	 * @author 黄国庆 2011-4-6 上午09:38:57
	 */
	protected static String resolvePath(Class<? extends ModulePluginDriver> loaderClass, String path) {
		if (path == null) return null;
		if (path.indexOf(":") > 0) return path;
		if (!path.startsWith("/")) {
			String baseName = loaderClass.getName();
			int index = baseName.lastIndexOf('.');
			if (index != -1) {
				baseName = "/" + baseName.substring(0, index);
			}
			if (path.length() != 0) {
				path = baseName + "/" + path;
			} else {
				path = baseName;
			}
		}
		return path;
	}

	private static void serializeDriverInfos(File file, boolean create) throws IOException, FileNotFoundException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		Enumeration<ModulePluginDriverInfo> driverInfos = getDriverInfos();
		List<Object> drivers = new ArrayList<Object>(8);
		while (driverInfos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = driverInfos.nextElement();
			drivers.add(driverInfo.getSerializableDriverInfo());
			if (create) {
				needUpdateDriverInfos.add(driverInfo);
				SystemLogUtils.secondPrintln("缓存" + driverInfo.getDriver() + "文件");
			}
		}
		oos.writeObject(drivers);
		oos.close();
	}
}