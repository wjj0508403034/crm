package com.huoyun.core.bo.validator.constraints;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.LocatableBusinessException;

public class StartsWith extends StringValidator {

	public StartsWith(BusinessObjectFacade boFacade, PropertyMeta propertyMeta, Object propertyValue) {
		super(boFacade, propertyMeta, propertyValue);
	}

	@Override
	public void validator() throws LocatableBusinessException {
		if (this.getPropertyValue() == null || this.getPropertyValue().getClass() != String.class) {
			throw validatorException();
		}

		if (!StringUtils.startsWith((String) this.getPropertyValue(), this.getExpr())) {
			throw validatorException();
		}
	}

}
