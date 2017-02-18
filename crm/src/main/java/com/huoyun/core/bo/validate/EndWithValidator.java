package com.huoyun.core.bo.validate;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class EndWithValidator extends AbstractValidator {

	public EndWithValidator(BusinessObjectFacade boFacade, PropertyMeta propertyMeta) {
		super(boFacade, propertyMeta);
	}

	@Override
	public void validator(Object value) throws BusinessException {
		String suffix = propertyMeta.getValidationRule().substring(3);
		String errorMessage = boFacade.getLocaleService().getMessage(ValidationErrorCode.EndWith,
				new Object[] { suffix });
		if (value == null || value.getClass() != String.class) {
			throw validatorException(errorMessage);
		}

		if (!StringUtils.endsWith((String) value, suffix)) {
			throw validatorException(errorMessage);
		}
	}

}
