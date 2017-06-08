package com.huoyun.core.bo.impl;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.BoSpecification;

public class BoRepositoryImpl<T extends BusinessObject> extends
		AbstractBoRepository<T> {

	public BoRepositoryImpl(Class<T> boType, BusinessObjectFacade boFacade,
			BoMeta boMeta) {
		super(boType, boFacade, boMeta);
	}


}
