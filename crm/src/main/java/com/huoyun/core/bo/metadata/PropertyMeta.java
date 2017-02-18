package com.huoyun.core.bo.metadata;

import java.lang.reflect.Field;

public interface PropertyMeta {

	String getName();

	String getNamespace();

	String getLabel();

	boolean isMandatory();

	boolean isReadonly();

	Field getField();
	
	Class<?> getRuntimeType();
	
	PropertyType getType();

	String getValidationRule();
	
	String getCustomErrorMessage();
}
