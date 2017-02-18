package com.huoyun.core.bo.metadata;

import org.joda.time.DateTime;

public enum PropertyType {
	None, String, Text, Email, Phone, DateTime, Number;

	public static PropertyType parse(Class<?> klass) {

		if (klass == String.class) {
			return PropertyType.String;
		}

		if (klass == DateTime.class) {
			return PropertyType.DateTime;
		}

		if (klass == int.class || klass == long.class || klass == Integer.class || klass == Long.class) {
			return PropertyType.Number;
		}

		return PropertyType.None;
	}
}
