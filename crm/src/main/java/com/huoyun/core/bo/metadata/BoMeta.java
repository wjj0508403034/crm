package com.huoyun.core.bo.metadata;

import java.util.List;

import com.huoyun.core.bo.BusinessObject;

public interface BoMeta {

	Class<BusinessObject> getBoType();
	
	List<PropertyMeta> getProperties();
	
	boolean hasProperty(String propertyName);
	
	PropertyMeta getPropertyMeta(String propertyName);

	String getExtTableName();

	String getName();
	
	String getNamespace();
	
	String getLabel();

	boolean isAllowCustomized();

	String getBusinessKey();

	String getPrimaryKey();
}
