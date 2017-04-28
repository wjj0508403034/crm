package com.huoyun.core.bo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.core.bo.validator.ValidatorFactory;
import com.huoyun.locale.LocaleService;

public interface BusinessObjectFacade {

	<T extends BusinessObject> T newBo(Class<T> boType);

	BusinessObject newBo(String namespace, String name);

	MetadataRepository getMetadataRepository();

	<T extends BusinessObject> BoRepository<T> getBoRepository(Class<T> boType);

	<T extends BusinessObject> BoRepository<T> getBoRepository(String namespace, String name);

	EntityManager getEntityManager();

	EntityManagerFactory getEntityManagerFactory();

	LocaleService getLocaleService();

	ExtensionService getExtensionService();

	EntityManager getCurrentEntityManager();
	
	ValidatorFactory getValidatorFactory();
	
	<T> T getBean(Class<T> klass);
	
	Employee getCurrentEmployee();

	Long getUserId();
}
