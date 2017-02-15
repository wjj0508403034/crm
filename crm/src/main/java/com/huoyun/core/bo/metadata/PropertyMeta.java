package com.huoyun.core.bo.metadata;

public interface PropertyMeta {

	String getName();

	String getNamespace();

	String getLabel();

	boolean isMandatory();

	boolean isReadonly();
}
