package com.huoyun.business.permission.impl;

import com.huoyun.business.permission.PermissionService;
import com.huoyun.core.bo.BusinessObjectFacade;

public class PermissionServiceImpl implements PermissionService {

	private BusinessObjectFacade boFacade;

	public PermissionServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

}
