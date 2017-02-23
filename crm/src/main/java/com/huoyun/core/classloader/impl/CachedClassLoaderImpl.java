package com.huoyun.core.classloader.impl;

import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.ExtensibleBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.core.classloader.CachedClassLoader;

public class CachedClassLoaderImpl implements CachedClassLoader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CachedClassLoaderImpl.class);

	private Reflections reflections;
	private final Map<String, Class<? extends BusinessObject>> boClassCache = new HashMap<>();
	private final Map<String, Class<? extends ExtensibleBusinessObject>> extBoClassCache = new HashMap<>();

	public static CachedClassLoader instance(){
		return new CachedClassLoaderImpl();
	}
	
	public CachedClassLoaderImpl() {
		this.initialize();
	}

	@SuppressWarnings("unchecked")
	private void initialize() {
		LOGGER.info("========start to initialize class==========");
		Predicate<String> filter = new FilterBuilder().include(FilterBuilder
				.prefix("com.huoyun"));
		reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("com.huoyun"))
				.filterInputsBy(filter)
				.setScanners(new SubTypesScanner(),
						new TypeAnnotationsScanner()).useParallelExecutor());

		for (Class<?> clazz : reflections.getTypesAnnotatedWith(BoEntity.class)) {
			String key = BusinessObjectUtils.getBoFullName(clazz);
			if(BusinessObject.class.isAssignableFrom(clazz)){
				this.boClassCache.put(key, (Class<? extends BusinessObject>) clazz);
				
				if(ExtensibleBusinessObject.class.isAssignableFrom(clazz)){
					this.extBoClassCache.put(key, (Class<? extends ExtensibleBusinessObject>) clazz);
				}
			}
		}

		LOGGER.info("========end to initialize class==========");
	}

	@Override
	public Map<String, Class<? extends BusinessObject>> getBoClassCache() {
		return this.boClassCache;
	}

	@Override
	public Map<String, Class<? extends ExtensibleBusinessObject>> getExtBoClassCache() {
		return this.extBoClassCache;
	}
}
