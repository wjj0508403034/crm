package com.huoyun.core.bo.validate;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;

public class ValidatorUtils {

	public static Validator getValidator(BusinessObjectFacade boFacade, PropertyMeta propMeta) {
		if (propMeta.getType() == PropertyType.Email) {
			return new EmailValidator(boFacade, propMeta);
		}
		if (StringUtils.isEmpty(propMeta.getValidationRule())) {
			return null;
		}

		if (propMeta.getType() == PropertyType.Number) {
			return null;
		}

		if (propMeta.getType() == PropertyType.String || propMeta.getType() == PropertyType.Text) {
			if (StringUtils.startsWithIgnoreCase(propMeta.getValidationRule(), "sw=")) {
				return new StartWithValidator(boFacade, propMeta);
			}
			
			if (StringUtils.startsWithIgnoreCase(propMeta.getValidationRule(), "ew=")) {
				return new EndWithValidator(boFacade, propMeta);
			}
		}

		return null;
	}
}
