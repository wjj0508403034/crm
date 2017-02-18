package com.huoyun.core.bo.validate;

import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.LocatableBusinessException;

public class EmailValidator extends AbstractValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailValidator.class);

	public EmailValidator(BusinessObjectFacade boFacade, PropertyMeta propertyMeta) {
		super(boFacade, propertyMeta);
	}

	@Override
	public void validator(Object value) throws LocatableBusinessException {
		String errorMessage = boFacade.getLocaleService().getMessage(ValidationErrorCode.Email);
		if (value == null || value.getClass() != String.class) {
			throw validatorException(errorMessage);
		}
		String email = (String) value;
		try {
			new InternetAddress(email, true);
		} catch (Exception ex) {
			LOGGER.error(String.format("Email %s format invalid.", email), ex);
			throw validatorException(errorMessage);
		}
	}

}
