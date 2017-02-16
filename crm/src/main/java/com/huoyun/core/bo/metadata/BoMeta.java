package com.huoyun.core.bo.metadata;

import java.util.List;

import com.huoyun.core.bo.BusinessObject;

public interface BoMeta {

	Class<BusinessObject> getBoType();
	
	List<PropertyMeta> getProperties();
}
