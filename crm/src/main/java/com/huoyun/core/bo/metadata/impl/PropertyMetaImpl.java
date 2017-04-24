package com.huoyun.core.bo.metadata.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BoPropertyRule;
import com.huoyun.core.bo.annotation.ValidValue;
import com.huoyun.core.bo.annotation.ValidValues;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.metadata.ValidationMeta;
import com.huoyun.core.bo.metadata.Value;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.locale.LocaleService;

public class PropertyMetaImpl implements PropertyMeta {

	public PropertyMetaImpl(String boName, Field field, LocaleService localeService) {
		this.field = field;
		this.localeService = localeService;
		BoProperty boProp = field.getAnnotation(BoProperty.class);
		if (boProp == null) {
			throw new RuntimeException("No BoProperty annotation");
		}

		this.readonly = boProp.readonly();
		this.searchable = boProp.searchable();
		this.mandatory = boProp.mandatory();

		this.type = boProp.type();
		if (this.type == PropertyType.None) {
			this.type = PropertyType.parse(this.field.getType());
		}

		if (this.type == PropertyType.BoLabel) {
			this.additionInfo.put("boNamespace", BusinessObjectUtils.getBoNamespace(this.field.getType()));
			this.additionInfo.put("boName", BusinessObjectUtils.getBoName(this.field.getType()));
		}

		this.name = field.getName();
		if (StringUtils.isEmpty(boProp.label())) {
			this.labelI18nKey = "bo.label.prop." + boName + "." + this.name;
		} else {
			this.labelI18nKey = boProp.label();
		}
		this.label = this.localeService.getMessage(this.labelI18nKey);

		BoPropertyRule propRule = field.getAnnotation(BoPropertyRule.class);
		if (propRule != null) {
			this.validationMeta = new ValidationMetaImpl(propRule);
		}

		ValidValues validValuesAnno = field.getAnnotation(ValidValues.class);
		if (validValuesAnno != null) {
			if (validValuesAnno.validValues() != null && validValuesAnno.validValues().length > 0) {
				this.validationMeta = null;
				for (ValidValue validValue : validValuesAnno.validValues()) {
					Value value = new Value();
					value.setName(validValue.value());
					String valueLabelKey = "bo.label.prop." + boName + "." + this.name + ".validvalues."
							+ validValue.value();
					value.setLabel(this.localeService.getMessage(valueLabelKey));
					this.validValues.add(value);
				}
			}
		}

	}

	private String name;
	private String namespace = BusinessObjectUtils.SYSTEM_BO_NAMESPACE;
	private String label;
	private String labelI18nKey;
	private LocaleService localeService;
	private boolean mandatory;
	private boolean readonly;
	private boolean searchable;
	private boolean nullable;
	private Field field;
	private PropertyType type;
	private ValidationMeta validationMeta;
	private List<Value> validValues = new ArrayList<>();
	private Map<String, Object> additionInfo = new HashMap<>();

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	@JsonIgnore
	@Override
	public Field getField() {
		return field;
	}

	@JsonIgnore
	@Override
	public Class<?> getRuntimeType() {
		return this.field.getType();
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public PropertyType getType() {
		return this.type;
	}

	@JsonIgnore
	@Override
	public String getColumnName() {
		return null;
	}

	@Override
	public boolean isCustomField() {
		return false;
	}

	@Override
	public ValidationMeta getValidationMeta() {
		return validationMeta;
	}

	public void setValidationMeta(ValidationMeta validationMeta) {
		this.validationMeta = validationMeta;
	}

	@Override
	public List<Value> getValidValues() {
		return validValues;
	}

	@Override
	public Map<String, Object> getAdditionInfo() {
		return additionInfo;
	}

	public void setAdditionInfo(Map<String, Object> additionInfo) {
		this.additionInfo = additionInfo;
	}

	@Override
	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
}
