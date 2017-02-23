package com.huoyun.core.bo.metadata.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.core.classloader.CachedClassLoader;
import com.huoyun.locale.LocaleService;

public class MetadataRepositoryImpl implements MetadataRepository {

	public static final String Name = "SystemMetadataRepository";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MetadataRepositoryImpl.class);

	private LocaleService localeService;
	private CachedClassLoader classLoader;
	private final Map<String, BoMeta> boMetaCache = new HashMap<>();

	public MetadataRepositoryImpl(LocaleService localeService,
			CachedClassLoader classLoader) {
		this.localeService = localeService;
		this.classLoader = classLoader;
		initialize();
	}

	private void initialize() {
		LOGGER.info("========start to initialize BO metadata==========");
		for (String key : this.classLoader.getBoClassCache().keySet()) {
			Class<? extends BusinessObject> boClass = this.classLoader
					.getBoClassCache().get(key);
			this.boMetaCache.put(key, new BoMetaImpl(boClass, this.localeService));
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

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
