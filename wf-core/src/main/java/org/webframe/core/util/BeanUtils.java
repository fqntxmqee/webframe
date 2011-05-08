
package org.webframe.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 继承org.springframework.beans.BeanUtils 扩展相关方法
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午09:34:58
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

	private static Log	log	= LogFactory.getLog(BeanUtils.class);

	/**
	 * 调用bean中的对应set方法赋值， 如果bean中的set方法不存在map中的键，忽略该键赋值 支持Properties
	 * 
	 * @param bean bean对象
	 * @param map map对象
	 * @author 黄国庆 2010-4-20 下午04:18:33
	 */
	public static void setBeanProperties(Object bean, Map<? extends Object, ? extends Object> map) {
		if (map == null) return;
		PropertyDescriptor[] targetPds = getPropertyDescriptors(bean.getClass());
		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null) {
				Object value = map.get(targetPd.getName());
				if (value == null) continue;
				Class<?>[] parameterTypes = writeMethod.getParameterTypes();
				if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
					writeMethod.setAccessible(true);
				}
				try {
					if (parameterTypes[0].isPrimitive()) {
						if (parameterTypes[0] == Integer.TYPE) {
							value = Integer.parseInt(value.toString());
						} else if (parameterTypes[0] == Boolean.TYPE) {
							value = Boolean.parseBoolean(value.toString());
						} else if (parameterTypes[0] == Character.TYPE) {
							value = Character.valueOf(value.toString().charAt(0));
						} else if (parameterTypes[0] == Byte.TYPE) {
							value = Byte.parseByte(value.toString());
						} else if (parameterTypes[0] == Short.TYPE) {
							value = Short.parseShort(value.toString());
						} else if (parameterTypes[0] == Long.TYPE) {
							value = Long.parseLong(value.toString());
						} else if (parameterTypes[0] == Float.TYPE) {
							value = Float.parseFloat(value.toString());
						} else if (parameterTypes[0] == Double.TYPE) {
							value = Double.parseDouble(value.toString());
						} else {
							throw new NumberFormatException();
						}
					}
					writeMethod.invoke(bean, new Object[]{
						value});
				} catch (NumberFormatException e) {
					log.error(e.getMessage(), e);
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
}
