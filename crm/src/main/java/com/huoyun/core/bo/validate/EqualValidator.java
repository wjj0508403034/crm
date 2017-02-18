package com.huoyun.core.bo.validate;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class EqualValidator extends AbstractValidator {

	public EqualValidator(BusinessObjectFacade boFacade, PropertyMeta propertyMeta) {
		super(boFacade, propertyMeta);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void validator(Object value) throws BusinessException {
		// TODO Auto-generated method stub

	}

}
