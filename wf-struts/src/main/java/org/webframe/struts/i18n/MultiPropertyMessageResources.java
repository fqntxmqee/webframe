
package org.webframe.struts.i18n;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;
import org.webframe.support.driver.resource.jar.JarResourcePatternResolver;

/**
 * 支持多国际化配置文件
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-10 下午4:39:00
 * @version
 */
public class MultiPropertyMessageResources extends PropertyMessageResources {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -54996936374817674L;

	public MultiPropertyMessageResources(MessageResourcesFactory factory,
				String config,
				boolean returnNull) {
		super(factory, config, returnNull);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void loadLocale(String localeKey) {
		if (log.isTraceEnabled()) {
			log.trace("loadLocale(" + localeKey + ")");
		}
		// Have we already attempted to load messages for this locale?
		if (locales.get(localeKey) != null) {
			return;
		}
		locales.put(localeKey, localeKey);
		// Set up to load the property resource for this locale key, if we can
		String name = config.replace('.', '/');
		if (localeKey.length() > 0) {
			name += "_" + localeKey;
		}
		name += ".properties";
		// Load the specified property resource
		if (log.isTraceEnabled()) {
			log.trace("  Loading resource '" + name + "'");
		}
		Properties props = getAllProperties(name);
		if (log.isTraceEnabled()) {
			log.trace("  Loading resource completed");
		}
		// Copy the corresponding values into our cache
		if (props.size() < 1) {
			return;
		}
		synchronized (messages) {
			Iterator<?> names = props.keySet().iterator();
			while (names.hasNext()) {
				String key = (String) names.next();
				if (log.isTraceEnabled()) {
					log.trace("  Saving message key '" + messageKey(localeKey, key));
				}
				messages.put(messageKey(localeKey, key), props.getProperty(key));
			}
		}
	}

	/**
	 * 采用依赖覆盖方式获取所有Properties
	 * 
	 * @param name
	 * @return
	 * @author 黄国庆 2012-5-10 下午4:54:27
	 */
	protected Properties getAllProperties(String name) {
		Properties props = new Properties();
		try {
			Collection<JarResourcePatternResolver> resolvers = JarResourcePatternResolver.getAllSortedResolver();
			for (JarResourcePatternResolver resolver : resolvers) {
				props.load(resolver.getResource(name).getInputStream());
			}
		} catch (IOException e) {
			log.error("getAllProperties()", e);
		}
		return props;
	}
}
