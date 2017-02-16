package com.huoyun.core.bo;

import com.huoyun.exception.LocatableBusinessException;

public interface BusinessObject {

	public Long getId();
	
	void init();
	
	void create();
	
	void update();
	
	void delete();

	void setPropertyValue(String propertyName, Object propertyValue) throws LocatableBusinessException;
	
	void setBoFacade(BusinessObjectFacade boFacade);
}
