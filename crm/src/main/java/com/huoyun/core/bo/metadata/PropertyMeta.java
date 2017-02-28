package com.huoyun.core.bo.metadata;

import java.lang.reflect.Field;
import java.util.List;

public interface PropertyMeta {

	String getName();

	String getNamespace();

	String getLabel();

	boolean isMandatory();

	boolean isReadonly();

	Field getField();
	
	Class<?> getRuntimeType();
	
	PropertyType getType();

	String getColumnName();
	
	boolean isCustomField();

	ValidationMeta getValidationMeta();
	
	List<Value> getValidValues();
}
