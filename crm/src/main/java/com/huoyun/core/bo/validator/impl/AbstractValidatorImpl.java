package com.huoyun.core.bo.validator.impl;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.ValidationMeta;
import com.huoyun.core.bo.validator.Validator;
import com.huoyun.exception.LocatableBusinessException;

public abstract class AbstractValidatorImpl implements Validator {
	protected final BusinessObjectFacade boFacade;
	private final PropertyMeta propertyMeta;
	private final ValidationMeta validationMeta;
	private String errorMessage = null;
	private final Object propertyValue;

	public AbstractValidatorImpl(BusinessObjectFacade boFacade, PropertyMeta propertyMeta, Object propertyValue) {
		this.boFacade = boFacade;
		this.propertyMeta = propertyMeta;
		this.validationMeta = propertyMeta.getValidationMeta();
		this.propertyValue = propertyValue;
		if (this.validationMeta != null) {
			this.errorMessage = this.validationMeta.getErrorMessage();
		}
	}

	protected LocatableBusinessException validatorException() {
		if (!StringUtils.isEmpty(this.errorMessage)) {
			return new LocatableBusinessException(BoErrorCode.Bo_Property_Validator_Failed, this.errorMessage,
					propertyMeta.getName());

		}

		return null;
	}

	protected final Object getPropertyValue() {
		return propertyValue;
	}

	protected final PropertyMeta getPropertyMeta() {
		return this.propertyMeta;
	}
}
