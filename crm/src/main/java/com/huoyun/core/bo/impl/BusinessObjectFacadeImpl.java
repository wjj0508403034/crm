package com.huoyun.core.bo.impl;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.BoRepository;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.core.bo.validator.ValidatorFactory;
import com.huoyun.core.multitenant.MultiTenantProperties;
import com.huoyun.locale.LocaleService;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.session.Session;

public class BusinessObjectFacadeImpl implements BusinessObjectFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessObjectFacadeImpl.class);
	private ApplicationContext context;
	private MetadataRepository metadataRepository;
	private EntityManager entityManager;
	private LocaleService localeService;
	private EntityManagerFactory emf;
	private final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<EntityManager>();

	public BusinessObjectFacadeImpl(ApplicationContext context) {
		this.context = context;
		this.metadataRepository = this.context.getBean(MetadataRepository.class);
		this.entityManager = this.context.getBean(EntityManager.class);
		this.localeService = this.context.getBean(LocaleService.class);
		this.emf = this.context.getBean(EntityManagerFactory.class);
	}

	@Override
	public <T extends BusinessObject> T newBo(Class<T> boType) {
		T bo = null;
		try {
			bo = boType.getConstructor(BusinessObjectFacade.class).newInstance(this);
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
			throw new RuntimeException(String.format("Entity {0} {1} not found", namespace, name));
		}

		return this.newBo(boMeta.getBoType());
	}

	@Override
	public MetadataRepository getMetadataRepository() {
		return this.metadataRepository;
	}

	@Override
	public <T extends BusinessObject> BoRepository<T> getBoRepository(Class<T> boType) {
		BoMeta boMeta = this.metadataRepository.getBoMeta(boType);
		if (boMeta == null) {
			throw new RuntimeException(String.format("Entity {0} not found", boType));
		}
		if (AbstractBusinessObjectImpl.class.isAssignableFrom(boType)) {
			return new BoRepositoryImpl(boType, this, boMeta);
			// this.repoCache.put(boType, repo);
		} else if (LiteBusinessObject.class.isAssignableFrom(boType)) {
			return new BoRepositoryImpl(boType, this, boMeta);
		}
		return null;
	}

	@Override
	public <T extends BusinessObject> BoRepository<T> getBoRepository(String namespace, String name) {
		BoMeta boMeta = this.metadataRepository.getBoMeta(namespace, name);
		if (boMeta == null) {
			throw new RuntimeException(String.format("Entity {0} {1} not found", namespace, name));
		}
		if (AbstractBusinessObjectImpl.class.isAssignableFrom(boMeta.getBoType())) {
			return new BoRepositoryImpl(boMeta.getBoType(), this, boMeta);
		} else if (LiteBusinessObject.class.isAssignableFrom(boMeta.getBoType())) {
			return new BoRepositoryImpl(boMeta.getBoType(), this, boMeta);
		}
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		if (StringUtils.isEmpty(this.getTenantCode())) {
			return this.entityManager;
		}
		
		EntityManager manager = threadLocal.get();
		if (manager == null || !manager.isOpen()) {
			try {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY, this.getTenantCode());
				manager = emf.createEntityManager(properties);
			} catch (Exception e) {
				Throwable cause = e.getCause();
				if (cause instanceof SQLInvalidAuthorizationSpecException) {
					LOGGER.error(cause.getMessage());
					Object jdbcUrl = emf.getProperties().get("javax.persistence.jdbc.url");
					LOGGER.error("javax.persistence.jdbc.url: {}", jdbcUrl);
					Object databaseUser = emf.getProperties().get("javax.persistence.jdbc.user");
					LOGGER.error("javax.persistence.jdbc.user: {}", databaseUser);
				}
				throw e;
			}

			threadLocal.set(manager);

		}
		return manager;
	}

	@Override
	public LocaleService getLocaleService() {
		return this.localeService;
	}

	@Override
	public ExtensionService getExtensionService() {
		return this.context.getBean(ExtensionService.class);
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return this.context.getBean(EntityManagerFactory.class);
	}

	@Override
	public ValidatorFactory getValidatorFactory() {
		return this.context.getBean(ValidatorFactory.class);
	}

	@Override
	public <T> T getBean(Class<T> klass) {
		return this.context.getBean(klass);
	}

	@Override
	public Employee getCurrentEmployee() {
		Session session = (Session) this.getHttpSsession().getAttribute(EndpointsConstatns.HuoYun_USER_SESSION);

		if (session != null) {
			return session.getEmployee();
		}
		return null;
	}

	@Override
	public Long getUserId() {
		Employee employee = this.getCurrentEmployee();
		if (employee != null) {
			return employee.getUserId();
		}
		return null;
	}

	private HttpSession getHttpSsession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	private String getTenantCode() {
		return (String) this.getHttpSsession().getAttribute(EndpointsConstatns.SSO_TENANT_CODE);
	}

}
