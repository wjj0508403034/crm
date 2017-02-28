package com.huoyun.core.bo.validator.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.validator.RuleType;
import com.huoyun.core.bo.validator.Validator;
import com.huoyun.core.bo.validator.ValidatorFactory;
import com.huoyun.core.bo.validator.constraints.Email;
import com.huoyun.core.bo.validator.constraints.Mandatory;
import com.huoyun.core.bo.validator.constraints.ValidValues;
import com.huoyun.exception.BusinessException;

public class ValidatorFactoryImpl implements ValidatorFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorFactoryImpl.class);
	private BusinessObjectFacade boFacade;

	public ValidatorFactoryImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public List<Validator> getValidators(PropertyMeta propMeta, Object propertyValue) throws BusinessException {
		List<Validator> validators = new ArrayList<>();
		if (propMeta.isMandatory()) {
			validators.add(new Mandatory(this.boFacade, propMeta, propertyValue));
		}

		if (propertyValue == null) {
			return validators;
		}

		if (propMeta.getType() == PropertyType.Email) {
			validators.add(new Email(this.boFacade, propMeta, propertyValue));
			return validators;
		}

		if (propMeta.getValidValues().size() > 0) {
			validators.add(new ValidValues(this.boFacade, propMeta, propertyValue));
			return validators;
		}

		if (propMeta.getValidationMeta() != null) {
			RuleType ruleType = propMeta.getValidationMeta().getRuleType();
			if (ruleType != null) {
				try {
					Constructor<Validator> constructor = ruleType.getValidatorClass()
							.getConstructor(BusinessObjectFacade.class, PropertyMeta.class);
					validators.add(constructor.newInstance(this.boFacade, propMeta, propertyValue));
				} catch (Exception e) {
					LOGGER.error("Get validation rule failed", e);
					throw new BusinessException(BoErrorCode.Bo_Property_Validator_Failed);
				}

			}
		}

		return validators;
	}
}
