package com.huoyun.core.bo.ext.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.ext.UserProperty;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.core.bo.utils.BusinessObjectUtils;

public class ExtMetadataRepositoryImpl implements MetadataRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExtMetadataRepositoryImpl.class);

	private MetadataRepository metadataRepository;
	private BusinessObjectFacade boFacade;
	private final Map<String, BoMeta> extMetaCache = new HashMap<>();

	public ExtMetadataRepositoryImpl(MetadataRepository metadataRepository,
			BusinessObjectFacade boFacade) {
		this.metadataRepository = metadataRepository;
		this.boFacade = boFacade;
	}

	@Override
	public BoMeta getBoMeta(String namespace, String name) {
		BoMeta boMeta = this.metadataRepository.getBoMeta(namespace, name);
		if (boMeta.isAllowCustomized()) {
			BoMeta extBoMeta = this.extMetaCache.get(BusinessObjectUtils
					.getFullName(namespace, name));
			if (extBoMeta != null) {
				return extBoMeta;
			}
		}

		return boMeta;
	}

	@Override
	public BoMeta getBoMeta(Class<?> clazz) {
		String key = BusinessObjectUtils.getBoFullName(clazz);
		BoMeta boMeta = this.metadataRepository.getBoMeta(clazz);
		if (boMeta.isAllowCustomized()) {
			BoMeta extBoMeta = this.extMetaCache.get(key);
			if (extBoMeta != null) {
				return extBoMeta;
			}
		}
		return boMeta;
	}

	@Override
	public void refresh() {
		LOGGER.info("========start to initialize ext BO metadata==========");
		List<UserProperty> userProperties = this.boFacade.getBoRepository(
				UserProperty.class).queryForList();
		for (UserProperty userProperty : userProperties) {
			String fullName = BusinessObjectUtils.getFullName(
					userProperty.getBoNamespace(), userProperty.getBoName());
			BoMeta boMeta = this.extMetaCache.get(fullName);
			if (boMeta != null) {
				((ExtBoMetaImpl) boMeta).mergeProperty(userProperty);
				continue;
			}

			boMeta = this.metadataRepository.getBoMeta(
					userProperty.getBoNamespace(), userProperty.getBoName());
			if (boMeta != null) {
				ExtBoMetaImpl extBoMeta = new ExtBoMetaImpl(boMeta);
				extBoMeta.mergeProperty(userProperty);
				this.extMetaCache.put(fullName, extBoMeta);
			} else {
				LOGGER.warn("Cann't find bometa: " + fullName);
			}

		}

		LOGGER.info("========end to initialize ext BO metadata==========");
	}

}
