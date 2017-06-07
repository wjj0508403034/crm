package com.huoyun.core.bo.metadata;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.exception.BusinessException;

public enum PropertyType {
	None, String, Text, Email, Phone, DateTime, Date, Number, BoLabel, Image, ImageList, Price, BoList, Color, Boolean;

	public static PropertyType parse(Class<?> klass) {

		if (klass == String.class) {
			return PropertyType.String;
		}

		if (klass == boolean.class || klass == Boolean.class) {
			return PropertyType.Boolean;
		}

		if (klass == DateTime.class) {
			return PropertyType.DateTime;
		}

		if (klass == int.class || klass == long.class || klass == Integer.class || klass == Long.class) {
			return PropertyType.Number;
		}

		if (BusinessObject.class.isAssignableFrom(klass)) {
			return PropertyType.BoLabel;
		}

		return PropertyType.None;
	}

	public static PropertyType parse(String type) throws BusinessException {
		if (StringUtils.equalsIgnoreCase("string", type)) {
			return PropertyType.String;
		}

		throw new BusinessException(BoErrorCode.Unkown_Bo_Property_Type);
	}
}
