package com.huoyun.core.bo.metadata.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.locale.LocaleService;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class MetadataRepositoryImpl implements MetadataRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MetadataRepositoryImpl.class);

	private LocaleService localeService;
	private Reflections reflections;
	private final Map<String, Class<?>> boClassCache = new HashMap<>();
	private final Map<String, BoMeta> boMetaCache = new HashMap<>();

	public MetadataRepositoryImpl(LocaleService localeService) {
		this.localeService = localeService;
		this.classLoader();
		initialize();
	}

	private void classLoader() {
		Predicate<String> filter = new FilterBuilder().include(FilterBuilder
				.prefix("com.huoyun"));
		reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("com.huoyun"))
				.filterInputsBy(filter)
				.setScanners(new SubTypesScanner(),
						new TypeAnnotationsScanner()).useParallelExecutor());

		for (Class<?> clazz : reflections.getTypesAnnotatedWith(BoEntity.class)) {
			String key = BusinessObjectUtils.getBoFullName(clazz);

			this.boClassCache.put(key, clazz);
		}
	}

	private void initialize() {
		LOGGER.info("========start to initialize BO metadata==========");
		for (String key : this.boClassCache.keySet()) {
			this.boMetaCache.put(key,
					new DefaultBoMeta(this.boClassCache.get(key),
							this.localeService));
		}
		LOGGER.info("========end to initialize BO metadata==========");
	}

	@Override
	public BoMeta getBoMeta(String namespace, String name) {
		return this.boMetaCache.get(BusinessObjectUtils.getFullName(namespace,
				name));
	}

	@Override
	public BoMeta getBoMeta(Class<?> clazz) {
		String key = BusinessObjectUtils.getBoFullName(clazz);
		return this.boMetaCache.get(key);
	}

}
