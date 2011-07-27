/*
 * wf-core
 * Created on 2011-5-6-下午09:56:46
 */

package org.webframe.core.hibernate;

import java.io.IOException;
import java.util.Enumeration;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.MappingException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.webframe.support.driver.ModulePluginDriver;
import org.webframe.support.driver.ModulePluginDriverInfo;
import org.webframe.support.driver.ModulePluginUtils;
import org.webframe.support.util.ClassUtils;
import org.webframe.support.util.SystemLogUtils;

/**
 * HibernateEntity自动加载工具类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午09:56:46
 */
public class HibernateEntityUtils extends ModulePluginUtils {

	protected static final String	RESOURCE_PATTERN_ENTITY		= "/**/*.class";

	protected static final String	RESOURCE_PATTERN_HBM			= "/**/*.hbm.xml";

	private static TypeFilter[]	defaultEntityTypeFilters	= new TypeFilter[]{
				new AnnotationTypeFilter(Entity.class, false), new AnnotationTypeFilter(Embeddable.class, false),
				new AnnotationTypeFilter(MappedSuperclass.class, false),
				new AnnotationTypeFilter(org.hibernate.annotations.Entity.class, false)};

	/**
	 * 加载模块中的注解Entity class
	 * 
	 * @param config AnnotationConfiguration实例
	 * @param entityTypeFilters 过滤指定需要加载的AnnotationType数组过滤器集合
	 * @author 黄国庆 2011-5-8 下午01:47:39
	 */
	static void addAnnotatedClass(AnnotationConfiguration config, TypeFilter[] entityTypeFilters) {
		Enumeration<ModulePluginDriverInfo> dirverInfos = getDriverInfos();
		SystemLogUtils.rootPrintln("Annotation 加载Entity开始！");
		while (dirverInfos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = dirverInfos.nextElement();
			SystemLogUtils.secondPrintln(driverInfo.getDriver() + "加载Entity！");
			Resource[] resources = HibernateEntityUtils.getEntityResources(driverInfo, RESOURCE_PATTERN_ENTITY);
			if (resources == null) continue;
			try {
				ClassRelativeResourceLoader classRelativeResourceLoader = new ClassRelativeResourceLoader(driverInfo.getClass());
				ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(classRelativeResourceLoader);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if (matchesFilter(reader, readerFactory, entityTypeFilters)) {
							config.addAnnotatedClass(resourcePatternResolver.getClassLoader().loadClass(className));
							SystemLogUtils.thirdPrintln("Entity：" + className);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		SystemLogUtils.rootPrintln("Annotation 加载Entity结束！");
	}

	static void addJarHbm(Configuration config) {
		Enumeration<ModulePluginDriverInfo> dirverInfos = getDriverInfos();
		SystemLogUtils.rootPrintln("加载Hibernate hbm开始！");
		while (dirverInfos.hasMoreElements()) {
			ModulePluginDriverInfo driverInfo = dirverInfos.nextElement();
			SystemLogUtils.secondPrintln(driverInfo.getDriver() + "加载hbm！");
			Resource[] resources = HibernateEntityUtils.getEntityResources(driverInfo, RESOURCE_PATTERN_HBM);
			if (resources == null) continue;
			try {
				for (Resource resource : resources) {
					config.addInputStream(resource.getInputStream());
					SystemLogUtils.thirdPrintln("Entity：" + resource.getFilename());
				}
			} catch (MappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SystemLogUtils.rootPrintln("加载Hibernate hbm结束！");
	}

	/**
	 * 获取可能是Entity的class文件资源
	 * 
	 * @param driver 模块插件驱动
	 * @param resourceType 匹配文件类型
	 * @return 可能为null
	 * @author 黄国庆 2011-4-5 下午04:33:40
	 */
	public static Resource[] getEntityResources(ModulePluginDriverInfo driverInfo, String resourceType) {
		ModulePluginDriver driver = driverInfo.getDriver();
		Class<? extends ModulePluginDriver> loaderClass = driver.getClass();
		String entityLocation = resolvePath(loaderClass, driver.getEntityLocation());
		if (entityLocation == null) return null;
		String pattern = ClassUtils.convertClassNameToResourcePath(entityLocation) + resourceType;
		return getResources(driverInfo, pattern);
	}

	private static boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory, TypeFilter[] entityTypeFilters)
				throws IOException {
		entityTypeFilters = entityTypeFilters == null ? defaultEntityTypeFilters : entityTypeFilters;
		for (TypeFilter filter : entityTypeFilters) {
			if (filter.match(reader, readerFactory)) {
				return true;
			}
		}
		return false;
	}
}
