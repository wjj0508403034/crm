package com.huoyun.core.bo.validate;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.LocatableBusinessException;

public class StartWithValidator extends AbstractValidator {

	public StartWithValidator(BusinessObjectFacade boFacade, PropertyMeta propertyMeta) {
		super(boFacade, propertyMeta);
	}

	@Override
	public void validator(Object value) throws LocatableBusinessException {
		String prefix = propertyMeta.getValidationRule().substring(3);
		String errorMessage = boFacade.getLocaleService().getMessage(ValidationErrorCode.StartWith,
				new Object[] { prefix });
		if (value == null || value.getClass() != String.class) {
			throw validatorException(errorMessage);
		}

		if (!StringUtils.startsWith((String) value, prefix)) {
			throw validatorException(errorMessage);
		}
	}

}
