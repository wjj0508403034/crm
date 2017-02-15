package com.huoyun.core.bo;

public interface BusinessObjectFacade {

	 <T extends BusinessObject> T newBo(Class<T> boType);
	 
	 BusinessObject newBo(String namespace, String name);
}
