package com.huoyun.core.bo.validator.constraints;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.validator.ErrorCode;
import com.huoyun.core.bo.validator.impl.AbstractValidatorImpl;
import com.huoyun.exception.LocatableBusinessException;

public abstract class StringValidator extends AbstractValidatorImpl {

	private String expr;

	public StringValidator(BusinessObjectFacade boFacade, PropertyMeta propertyMeta, Object propertyValue) {
		super(boFacade, propertyMeta, propertyValue);

		this.expr = propertyMeta.getValidationMeta().getExpr();
	}

	protected String getExpr() {
		return this.expr;
	}

	@Override
	protected LocatableBusinessException validatorException() {
		LocatableBusinessException ex = super.validatorException();
		if (ex == null) {
			String errorMessage = boFacade.getLocaleService().getMessage(ErrorCode.StartWith,
					new Object[] { this.getExpr() });
			ex = new LocatableBusinessException(BoErrorCode.Bo_Property_Validator_Failed, errorMessage,
					this.getPropertyMeta().getName());
		}

		return ex;
	}

}
