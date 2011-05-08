
package org.webframe.core.util;

import java.util.Collection;
import java.util.Set;

import org.webframe.core.hibernate.EntityUtil;

/**
 * 业务模型实体工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-5
 *          下午01:28:51
 */
public abstract class EntityUtils extends EntityUtil {

	public static Set<String> getEntitiesName() {
		return entityMap.keySet();
	}

	public static Collection<Class<?>> getEntitiesClass() {
		return entityMap.values();
	}

	public static boolean hasEntityName(String entityName) {
		return entityMap.containsKey(entityName);
	}

	public static Class<?> getEntityClass(String entityName) {
		return entityMap.get(entityName);
	}
}
