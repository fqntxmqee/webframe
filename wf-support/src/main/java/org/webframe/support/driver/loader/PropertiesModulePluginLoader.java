
package org.webframe.support.driver.loader;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.util.ResourceUtils;
import org.webframe.support.util.StopWatch;

/**
 * Properties模块插件驱动加载器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-5 下午02:39:10
 */
public class PropertiesModulePluginLoader extends AbstractModulePluginLoader {

	/**
	 * 模块插件驱动properties文件路径
	 */
	private String							modulePluginProperties;

	private List<URLConnection>		urlConnections		= new ArrayList<URLConnection>();

	private List<JarURLConnection>	jarURLConnections	= new ArrayList<JarURLConnection>();

	private boolean						defaultSorted		= true;

	public PropertiesModulePluginLoader() {
		this("modulePlugin.properties");
	}

	public PropertiesModulePluginLoader(String modulePluginProperties) {
		setModulePluginProperties(modulePluginProperties);
	}

	public void setDefaultSorted(boolean defaultSorted) {
		this.defaultSorted = defaultSorted;
	}

	protected final String getModulePluginProperties() {
		return modulePluginProperties;
	}

	public void setModulePluginProperties(String modulePluginProperties) {
		this.modulePluginProperties = modulePluginProperties;
	}

	/* (non-Javadoc)
	 * @see org.webframe.support.driver.loader.ModulePluginLoader#loadModulePlugin()
	 */
	@Override
	public void loadModulePlugin() throws DriverNotExistException {
		try {
			StopWatch stopWatch = StopWatch.getStopWatch("加载模块驱动插件类", false);
			stopWatch.start("加载模块插件类properties配置。。。");
			loadPropertiesJar();
			stopWatch.stop();
			stopWatch.start("模块插件jar包进行排序。。。");
			Collection<JarURLConnection> sortUrls = sorted();
			Map<Object, Integer> driverMap = loadProperties(sortUrls);
			String[] drivers = sortProperties(driverMap);
			stopWatch.stop();
			stopWatch.start("开始加载模块驱动插件类。。。");
			loadModulePlugin(drivers);
			stopWatch.stop();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	protected Collection<JarURLConnection> sorted() {
		if (defaultSorted) {
			Collection<JarURLConnection> sortUrls;
			try {
				sortUrls = getModulePluginSorter().sort(jarURLConnections);
				if (sortUrls.size() != jarURLConnections.size()) {
					sortUrls = jarURLConnections;
				}
			} catch (IOException e) {
				sortUrls = jarURLConnections;
			}
			return sortUrls;
		} else {
			return jarURLConnections;
		}
	}

	/**
	 * 排序properties，并返回按照排序后properties内容
	 * 
	 * @param driverMap
	 * @return
	 * @author 黄国庆 2012-4-28 上午8:28:57
	 */
	protected String[] sortProperties(Map<Object, Integer> driverMap) {
		// 排序
		List<Entry<Object, Integer>> mappingList = new ArrayList<Entry<Object, Integer>>(driverMap.entrySet());
		Collections.sort(mappingList, new Comparator<Entry<Object, Integer>>() {

			@Override
			public int compare(Entry<Object, Integer> m1, Entry<Object, Integer> m2) {
				return m1.getValue().compareTo(m2.getValue());
			}
		});
		List<String> driverList = new ArrayList<String>();
		for (Entry<Object, Integer> entry : mappingList) {
			driverList.add(entry.getKey().toString());
		}
		return driverList.toArray(new String[driverList.size()]);
	}

	/**
	 * 加载properties
	 * 
	 * @param sortUrls
	 * @return
	 * @throws IOException
	 * @author 黄国庆 2012-4-28 上午8:28:45
	 */
	protected Map<Object, Integer> loadProperties(Collection<JarURLConnection> sortUrls)
				throws IOException {
		Map<Object, Integer> driverMap = new HashMap<Object, Integer>();
		Integer increaseKey = 0;
		for (JarURLConnection jarURLConnection : sortUrls) {
			Properties properties = new Properties();
			properties.load(jarURLConnection.getInputStream());
			for (Object key : properties.keySet()) {
				if ("1".equals(properties.get(key))) {
					increaseKey++;
					driverMap.put(key, increaseKey);
				}
			}
		}
		// override
		for (URLConnection urlConnection : urlConnections) {
			Properties properties = new Properties();
			properties.load(urlConnection.getInputStream());
			for (Object key : properties.keySet()) {
				if ("1".equals(properties.get(key))) {
					increaseKey++;
					driverMap.put(key, increaseKey);
				} else if (driverMap.containsKey(key)) {
					driverMap.remove(key);
				}
			}
		}
		return driverMap;
	}

	/**
	 * 初始化properties所在jar信息
	 * 
	 * @throws IOException
	 * @author 黄国庆 2012-4-28 上午8:29:49
	 */
	protected void loadPropertiesJar() throws IOException {
		List<URL> urls = ResourceUtils.getUrls(modulePluginProperties);
		for (URL url : urls) {
			URLConnection urlConnection = url.openConnection();
			if (urlConnection instanceof JarURLConnection) {
				jarURLConnections.add((JarURLConnection) urlConnection);
			} else {
				urlConnections.add(urlConnection);
			}
		}
	}
}
