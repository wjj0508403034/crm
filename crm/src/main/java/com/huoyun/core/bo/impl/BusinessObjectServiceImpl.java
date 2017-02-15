package com.huoyun.core.bo.impl;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectService;

public class BusinessObjectServiceImpl implements BusinessObjectService {

	private BusinessObjectFacade boFacade;

	public BusinessObjectServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public BusinessObject initBo(String namespace, String name) {
		return this.boFacade.newBo(namespace, name);
	}

}
