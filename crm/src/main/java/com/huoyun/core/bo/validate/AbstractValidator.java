package com.huoyun.core.bo.validate;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.LocatableBusinessException;

public abstract class AbstractValidator implements Validator {
	protected final BusinessObjectFacade boFacade;
	protected final PropertyMeta propertyMeta;

	public AbstractValidator(BusinessObjectFacade boFacade, PropertyMeta propertyMeta) {
		this.boFacade = boFacade;
		this.propertyMeta = propertyMeta;
	}

	protected final LocatableBusinessException validatorException(String errorMessage) {
		String message = errorMessage;
		if (!StringUtils.isEmpty(propertyMeta.getCustomErrorMessage())) {
			message = propertyMeta.getCustomErrorMessage();
		}
		return new LocatableBusinessException(BoErrorCode.Bo_Property_Validator_Failed, message,
				propertyMeta.getName());

	}

}
