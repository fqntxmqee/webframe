/*
 * wf-core
 * Created on 2011-5-6-下午09:50:10
 */

package org.webframe.core.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;
import org.webframe.core.util.EntityUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午09:50:10
 */
public abstract class EntityUtil {

	private static Log							log			= LogFactory.getLog(EntityUtils.class);

	protected static Map<String, Class<?>>	entityMap	= new HashMap<String, Class<?>>(0);

	static void init(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			entityMap = new HashMap<String, Class<?>>(0);
			return;
		}
		for (Object key : map.keySet()) {
			if (key == null) continue;
			String strKey = key.toString();
			// 去除包路径
			String moduleName = strKey.substring(strKey.lastIndexOf(".") + 1);
			// 转换Entity类首字母为小写；如果Entity类首字母为"T"，去除"T"，再将后面的字符串首字母小写。
			moduleName = moduleName.substring(moduleName.indexOf("T") + 1);
			if (moduleName.length() == 0) continue;
			String f = moduleName.substring(0, 1);
			String l = moduleName.substring(1);
			try {
				entityMap.put(f.toLowerCase() + l, ClassUtils.forName(strKey, EntityUtils.class.getClassLoader()));
			} catch (Throwable e) {
				log.error("类：" + strKey + " 不存在！", e);
			}
		}
	}
}
