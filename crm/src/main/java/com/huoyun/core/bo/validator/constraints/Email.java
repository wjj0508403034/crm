package com.huoyun.core.bo.validator.constraints;

import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.validator.ErrorCode;
import com.huoyun.core.bo.validator.impl.AbstractValidatorImpl;
import com.huoyun.exception.LocatableBusinessException;

public class Email extends AbstractValidatorImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(Email.class);

	public Email(BusinessObjectFacade boFacade, PropertyMeta propertyMeta, Object propertyValue) {
		super(boFacade, propertyMeta, propertyValue);
	}

	@Override
	public void validator() throws LocatableBusinessException {
		if (this.getPropertyValue() == null || this.getPropertyValue().getClass() != String.class) {
			throw validatorException();
		}
		String email = (String) this.getPropertyValue();
		try {
			new InternetAddress(email, true);
		} catch (Exception ex) {
			LOGGER.error(String.format("Email %s format invalid.", email), ex);
			throw validatorException();
		}
	}

	@Override
	protected LocatableBusinessException validatorException() {
		String errorMessage = boFacade.getLocaleService().getMessage(ErrorCode.Email);
		return new LocatableBusinessException(BoErrorCode.Bo_Property_Validator_Failed, errorMessage,
				this.getPropertyMeta().getName());
	}

}
