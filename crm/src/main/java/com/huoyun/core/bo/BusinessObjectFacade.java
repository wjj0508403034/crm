package com.huoyun.core.bo;

import javax.persistence.EntityManager;

import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.locale.LocaleService;

public interface BusinessObjectFacade {

	 <T extends BusinessObject> T newBo(Class<T> boType);
	 
	 BusinessObject newBo(String namespace, String name);
	 
	 MetadataRepository getMetadataRepository();
	 
	 <T extends BusinessObject> BoRepository<T> getBoRepository(Class<T> boType);
	 
	 <T extends BusinessObject> BoRepository<T> getBoRepository(String namespace, String name);
	 
	 EntityManager getEntityManager();
	 
	 LocaleService getLocaleService();
}
