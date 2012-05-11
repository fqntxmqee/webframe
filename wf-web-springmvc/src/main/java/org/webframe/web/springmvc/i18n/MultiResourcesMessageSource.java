
package org.webframe.web.springmvc.i18n;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.Resource;
import org.webframe.support.driver.resource.jar.JarResourcePatternResolver;

/**
 * 支持多国际化配置文件properties加载，根据框架的依赖，进行排序加载，或相关key覆盖
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-11 上午8:42:29
 * @version
 */
public class MultiResourcesMessageSource extends AbstractMessageSource {

	private final Map<String, Map<String, MessageFormat>>	localeMap	= new HashMap<String, Map<String, MessageFormat>>(8);

	// 资源路径必须以'/'开始，例如：'/i18n/messages'
	private String														basename		= null;

	public String getBasename() {
		return basename;
	}

	public void setBasename(String basename) {
		this.basename = basename;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.support.AbstractMessageSource#resolveCode(java.lang.String, java.util.Locale)
	 */
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat messageFormat = null;
		synchronized (localeMap) {
			String localeKey = getLocaleKey(locale);
			Map<String, MessageFormat> formatMap = localeMap.get(localeKey);
			if (formatMap == null) {
				String propertiesName = getPropertiesName(localeKey);
				Properties ps = getAllProperties(propertiesName);
				formatMap = new HashMap<String, MessageFormat>();
				Iterator<?> keys = ps.keySet().iterator();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					String msg = ps.getProperty(key);
					MessageFormat format = createMessageFormat(msg, locale);
					if (logger.isTraceEnabled()) {
						logger.trace("Saving message key " + format);
					}
					formatMap.put(key, format);
				}
				localeMap.put(localeKey, formatMap);
			}
			messageFormat = formatMap.get(code);
		}
		return messageFormat;
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
				Resource resource = resolver.getResource(name);
				if (resource.exists()) {
					props.load(resource.getInputStream());
				}
			}
		} catch (IOException e) {
			logger.error("getAllProperties()", e);
		}
		return props;
	}

	protected String getPropertiesName(String localeKey) {
		if (getBasename() == null) {
			setBasename("/i18n/messages");
		}
		String name = getBasename().replace('.', '/');
		if (localeKey.length() > 0) {
			name += "_" + localeKey;
		}
		name += ".properties";
		// Load the specified property resource
		if (logger.isTraceEnabled()) {
			logger.trace("  Loading resource '" + name + "'");
		}
		return name;
	}

	protected String getLocaleKey(Locale locale) {
		return (locale == null) ? "" : locale.toString();
	}
}
