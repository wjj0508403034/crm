package com.huoyun.core.bo.validator.constraints;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class StringLength extends StringValidator {

	public StringLength(BusinessObjectFacade boFacade, PropertyMeta propertyMeta, Object propertyValue) {
		super(boFacade, propertyMeta, propertyValue);

	}

	@Override
	public void validator() throws BusinessException {
		// TODO Auto-generated method stub

	}

}
