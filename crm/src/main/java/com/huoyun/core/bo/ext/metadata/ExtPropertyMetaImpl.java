package com.huoyun.core.bo.ext.metadata;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.ext.UserProperty;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;

public class ExtPropertyMetaImpl implements PropertyMeta {

	private PropertyMeta basePropertyMeta;
	private Long propertyId;
	private String label;
	private String name;
	private String namespace;
	private PropertyType type;
	private boolean readonly;

	public ExtPropertyMetaImpl(PropertyMeta basePropertyMeta) {
		this.basePropertyMeta = basePropertyMeta;
	}

	public ExtPropertyMetaImpl() {

	}

	@Override
	public String getName() {
		if (StringUtils.isEmpty(this.name)) {
			return this.basePropertyMeta.getName();
		}

		return this.name;
	}

	@Override
	public String getNamespace() {
		if (StringUtils.isEmpty(this.namespace)) {
			return this.basePropertyMeta.getNamespace();
		}

		return this.namespace;
	}

	@Override
	public String getLabel() {
		if (StringUtils.isEmpty(this.label)) {
			return this.basePropertyMeta.getLabel();
		}
		return this.label;
	}

	@Override
	public boolean isMandatory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReadonly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Field getField() {
		if (this.basePropertyMeta != null) {
			return this.basePropertyMeta.getField();
		}

		return null;
	}

	@Override
	public Class<?> getRuntimeType() {
		if (this.basePropertyMeta != null) {
			return this.basePropertyMeta.getRuntimeType();
		}

		return null;
	}

	@Override
	public PropertyType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidationRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnName() {
		return "STR1";
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void merge(UserProperty userProperty) {
		this.propertyId = userProperty.getId();
		this.label = userProperty.getLabel();
		this.name = userProperty.getName();
		this.namespace = userProperty.getNamespace();
		this.type = userProperty.getType();
		this.readonly = userProperty.isReadonly();
	}

	@Override
	public boolean isCustomField() {
		return this.propertyId != null;
	}

}
