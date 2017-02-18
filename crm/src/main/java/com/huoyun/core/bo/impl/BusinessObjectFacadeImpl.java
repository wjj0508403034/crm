package com.huoyun.core.bo.impl;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.huoyun.core.bo.BoRepository;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.DefaultBusinessObject;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.locale.LocaleService;

public class BusinessObjectFacadeImpl implements BusinessObjectFacade {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BusinessObjectFacadeImpl.class);
	private ApplicationContext context;
	private MetadataRepository metadataRepository;
	private EntityManager entityManager;
	private LocaleService localeService;

	public BusinessObjectFacadeImpl(ApplicationContext context) {
		this.context = context;
		this.metadataRepository = this.context
				.getBean(MetadataRepository.class);
		this.entityManager = this.context.getBean(EntityManager.class);
		this.localeService = this.context.getBean(LocaleService.class);;
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

		// if (bo != null) {
		// if (boType.getAnnotation(BoEntity.class) != null) {
		// BoMeta boMeta = this.metadataRepository.getBoMeta(boType);
		// }
		// }
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

	@Override
	public MetadataRepository getMetadataRepository() {
		return this.metadataRepository;
	}

	@Override
	public <T extends BusinessObject> BoRepository<T> getBoRepository(
			Class<T> boType) {
		BoMeta boMeta = this.metadataRepository.getBoMeta(boType);
		if (boMeta == null) {
			throw new RuntimeException(String.format("Entity {0} not found",
					boType));
		}
		if (DefaultBusinessObject.class.isAssignableFrom(boType)) {
			return new BoRepositoryImpl(boType, this, boMeta);
			// this.repoCache.put(boType, repo);
		} else if (LiteBusinessObject.class.isAssignableFrom(boType)) {
			// repo = new LiteBusinessObjectRepository(boType, boMeta, this);
			// this.repoCache.put(boType, repo);
		}
		return null;
	}

	@Override
	public <T extends BusinessObject> BoRepository<T> getBoRepository(
			String namespace, String name) {
		BoMeta boMeta = this.metadataRepository.getBoMeta(namespace, name);
		if (boMeta == null) {
			throw new RuntimeException(String.format(
					"Entity {0} {1} not found", namespace, name));
		}
		if (DefaultBusinessObject.class.isAssignableFrom(boMeta.getBoType())) {
			return new BoRepositoryImpl(boMeta.getBoType(), this, boMeta);
			// this.repoCache.put(boType, repo);
		} else if (LiteBusinessObject.class
				.isAssignableFrom(boMeta.getBoType())) {
			// repo = new LiteBusinessObjectRepository(boType, boMeta, this);
			// this.repoCache.put(boType, repo);
		}
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public LocaleService getLocaleService() {
		return this.localeService;
	}

}
