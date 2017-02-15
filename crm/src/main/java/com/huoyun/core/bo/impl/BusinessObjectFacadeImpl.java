package com.huoyun.core.bo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.MetadataRepository;

public class BusinessObjectFacadeImpl implements BusinessObjectFacade {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BusinessObjectFacadeImpl.class);
	private ApplicationContext context;
	private MetadataRepository metadataRepository;

	public BusinessObjectFacadeImpl(ApplicationContext context) {
		this.context = context;
		this.metadataRepository = this.context
				.getBean(MetadataRepository.class);
	}

	@Override
	public <T extends BusinessObject> T newBo(Class<T> boType) {
		T bo = null;
		try {
			bo = boType.getConstructor(BusinessObjectFacade.class).newInstance(
					this);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

//		if (bo != null) {
//			if (boType.getAnnotation(BoEntity.class) != null) {
//				BoMeta boMeta = this.metadataRepository.getBoMeta(boType);
//			}
//		}
		return bo;
	}

	@Override
	public BusinessObject newBo(String namespace, String name) {
		BoMeta boMeta = this.metadataRepository.getBoMeta(namespace, name);
		if (boMeta == null) {
			throw new RuntimeException(String.format(
					"Entity {0} {1} not found", namespace, name));
		}
		
		
		return this.newBo(boMeta.getBoType());
	}

}
