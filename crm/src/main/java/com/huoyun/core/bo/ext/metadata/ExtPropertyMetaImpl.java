package com.huoyun.core.bo.ext.metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huoyun.core.bo.ext.UserProperty;
import com.huoyun.core.bo.ext.UserPropertyValidValue;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.metadata.ValidationMeta;
import com.huoyun.core.bo.metadata.Value;

public class ExtPropertyMetaImpl implements PropertyMeta {

	private PropertyMeta basePropertyMeta;
	private Long propertyId;
	private String label;
	private String name;
	private String namespace;
	private PropertyType type;
	private boolean readonly;
	private String columnName;
	private List<Value> validValues = new ArrayList<>();

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
		return this.basePropertyMeta.isMandatory();
	}

	@Override
	public boolean isReadonly() {
		return this.readonly || this.basePropertyMeta.isReadonly();
	}

	@JsonIgnore
	@Override
	public Field getField() {
		if (this.basePropertyMeta != null) {
			return this.basePropertyMeta.getField();
		}

		return null;
	}

	@JsonIgnore
	@Override
	public Class<?> getRuntimeType() {
		if (this.basePropertyMeta != null) {
			return this.basePropertyMeta.getRuntimeType();
		}

		return null;
	}

	@Override
	public PropertyType getType() {
		return this.type;
	}

	@JsonIgnore
	@Override
	public String getColumnName() {
		return this.columnName;
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
		this.columnName = userProperty.getColumnName();
		this.validValues.clear();
		for (UserPropertyValidValue validValue : userProperty.getValidValues()) {
			Value value = new Value();
			value.setName(validValue.getValue());
			value.setLabel(validValue.getLabel());
			this.validValues.add(value);
		}
	}

	@Override
	public boolean isCustomField() {
		return this.propertyId != null;
	}

	@Override
	public ValidationMeta getValidationMeta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Value> getValidValues() {
		return this.validValues;
	}

	@Override
	public boolean isSearchable() {
		return this.basePropertyMeta.isSearchable();
	}

	@Override
	public Map<String, Object> getAdditionInfo() {
		return this.basePropertyMeta.getAdditionInfo();
	}

}
