package com.huoyun.core.bo.validator.constraints;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.validator.impl.AbstractValidatorImpl;
import com.huoyun.exception.BusinessException;
import com.huoyun.exception.LocatableBusinessException;

public class Mandatory extends AbstractValidatorImpl {

	public Mandatory(BusinessObjectFacade boFacade, PropertyMeta propertyMeta, Object propertyValue) {
		super(boFacade, propertyMeta, propertyValue);
	}

	@Override
	public void validator() throws BusinessException {
		if (this.getPropertyValue() == null) {
			throw new LocatableBusinessException(BoErrorCode.Bo_Property_Validator_Failed,
					this.getPropertyMeta().getName());
		}
	}

}
