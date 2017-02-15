package com.huoyun.core.bo.metadata;

public interface MetadataRepository {

	BoMeta getBoMeta(String namespace, String name);
	
	BoMeta getBoMeta(Class<?> clazz);
}
