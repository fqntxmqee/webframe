
package org.webframe.core.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 扩展PropertyPlaceholderConfigurer类，提供直接访问properties的方法
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-16 下午02:04:10
 */
public class PropertyConfigurerUtils extends PropertyPlaceholderConfigurer {

	private static Log							log					= LogFactory.getLog(PropertyConfigurerUtils.class);

	private static Properties					ps						= null;

	private static Map<Object, Properties>	propertiesCache	= new HashMap<Object, Properties>();

	@Override
	protected Properties mergeProperties() throws IOException {
		if (ps == null) {
			ps = super.mergeProperties();
		} else {
			ps.putAll(super.mergeProperties());
		}
		return ps;
	}

	/**
	 * 获取指定key的value 整数值
	 * 
	 * @param key
	 * @return 验证失败返回0
	 * @author: 黄国庆 2010-12-16 下午07:35:28
	 */
	public static int getInt(Object key) {
		if (!validate(key)) return 0;
		String result = ps.get(key).toString();
		try {
			return Integer.parseInt(result);
		} catch (NumberFormatException e) {
			log.error("指定key：" + key.toString() + " 的value值不为整数！", e);
			return 0;
		}
	}

	/**
	 * 获取指定key的value 浮点数值
	 * 
	 * @param key
	 * @return 验证失败，返回A constant holding a Not-a-Number (NaN) value of type
	 * @author: 黄国庆 2010-12-16 下午07:33:55
	 */
	public static double getDouble(Object key) {
		if (!validate(key)) return Double.NaN;
		String result = ps.get(key).toString();
		try {
			return Double.parseDouble(result);
		} catch (NumberFormatException e) {
			log.error("指定key：" + key.toString() + " 的value值不为浮点数！", e);
			return Double.NaN;
		}
	}

	/**
	 * 获取指定key的value bool值 Parses the string argument as a boolean. The <code>boolean</code> returned
	 * represents the value <code>true</code> if the string argument is not <code>null</code> and is
	 * equal, ignoring case, to the string {@code "true"}. <p> Example:
	 * {@code Boolean.parseBoolean("True")} returns <tt>true</tt>.<br> Example:
	 * {@code Boolean.parseBoolean("yes")} returns <tt>false</tt>.
	 * 
	 * @param key
	 * @return true or false
	 * @author: 黄国庆 2010-12-16 下午07:31:21
	 */
	public static boolean getBoolean(Object key) {
		if (!validate(key)) return false;
		String result = ps.get(key).toString();
		return Boolean.parseBoolean(result);
	}

	/**
	 * 获取指定key的value
	 * 
	 * @param key
	 * @return 验证失败返回空字符串
	 * @author: 黄国庆 2010-12-16 下午07:30:50
	 */
	public static String getString(Object key) {
		if (!validate(key)) return "";
		return ps.get(key).toString();
	}

	/**
	 * 获取指定key的value，并实例化该value值
	 * 
	 * @param key
	 * @return 实例化失败，返回null
	 * @author: 黄国庆 2010-12-16 下午07:29:58
	 */
	public static Object getObject(Object key) {
		if (!validate(key)) return null;
		String result = ps.get(key).toString();
		try {
			Class<?> clazz = PropertyConfigurerUtils.class.getClassLoader().loadClass(result);
			return clazz.newInstance();
		} catch (ClassNotFoundException e) {
			log.error("指定key：" + key.toString() + " 的value值为：" + result + "，实例化失败！", e);
		} catch (InstantiationException e) {
			log.error("指定key：" + key.toString() + " 的value值为：" + result + "，实例化失败！", e);
		} catch (IllegalAccessException e) {
			log.error("指定key：" + key.toString() + " 的value值为：" + result + "，实例化失败！", e);
		}
		return null;
	}

	/**
	 * 返回一个Properties，任何时候不会返回null
	 * 例如：org.mysql.url=mysql,org.mysql.username=test,org.oracle.url=oracle;
	 * getProperties("org.mysql") 返回 url=mysql,username=test Properties
	 * 
	 * @param key
	 * @return
	 * @author: 黄国庆 2011-1-17 上午10:43:54
	 */
	public static Properties getProperties(Object key) {
		if (propertiesCache.containsKey(key)) {
			return propertiesCache.get(key);
		}
		Properties res = getProperties(key, ps);
		propertiesCache.put(key, res);
		return res;
	}

	/**
	 * @param key
	 * @param properties
	 * @return
	 * @author: 黄国庆 2011-1-17 上午10:51:41
	 */
	public static Properties getProperties(Object key, Properties properties) {
		Properties res = new Properties();
		if (key == null) return res;
		for (Object _key : properties.keySet()) {
			if (_key == null) continue;
			String strKey = _key.toString();
			String token = key.toString() + ".";
			int index = strKey.lastIndexOf(token);
			if (index >= 0) {
				String newKey = strKey.replaceFirst(token, "");
				res.put(newKey, properties.get(_key));
			}
		}
		return res;
	}

	/**
	 * 验证Properties是否加载，是否存在指定key的value
	 * 
	 * @param key
	 * @return true or false
	 * @author: 黄国庆 2010-12-16 下午07:29:01
	 */
	private static boolean validate(Object key) {
		if (ps == null) {
			log.error("未加载任何Properties文件！");
			return false;
		}
		if (key == null) {
			log.error("指定key值不能null！");
			return false;
		}
		if (ps.get(key) == null) {
			log.error("指定key：" + key.toString() + " 不存在value值！");
			return false;
		}
		return true;
	}
}