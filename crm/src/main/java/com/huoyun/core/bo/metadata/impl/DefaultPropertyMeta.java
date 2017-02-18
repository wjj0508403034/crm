package com.huoyun.core.bo.metadata.impl;

import java.lang.reflect.Field;

import org.springframework.util.StringUtils;

import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.locale.LocaleService;

public class DefaultPropertyMeta implements PropertyMeta {

	public DefaultPropertyMeta(Field field, LocaleService localeService) {
		this.field = field;
		this.localeService = localeService;
		BoProperty boProp = field.getAnnotation(BoProperty.class);
		if (boProp == null) {
			throw new RuntimeException("No BoProperty annotation");
		}

		this.type = boProp.type();
		if (this.type == PropertyType.None) {
			this.type = PropertyType.parse(this.field.getType());
		}

		this.name = field.getName();
		if (StringUtils.isEmpty(boProp.label())) {
			this.labelI18nKey = "bo.label.prop." + this.name;
		} else {
			this.labelI18nKey = boProp.label();
		}
		this.label = this.localeService.getMessage(this.labelI18nKey);
		this.setValidationRule(boProp.validationRule());
	}

	private String name;
	private String namespace = BusinessObjectUtils.SYSTEM_BO_NAMESPACE;
	private String label;
	private String labelI18nKey;
	private LocaleService localeService;
	private boolean mandatory;
	private boolean readonly;
	private boolean nullable;
	private Field field;
	private PropertyType type;
	private String validationRule;

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

	@Override
	public Field getField() {
		return field;
	}

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

	@Override
	public String getValidationRule() {
		return validationRule;
	}

	public void setValidationRule(String validationRule) {
		this.validationRule = validationRule;
	}

	@Override
	public String getCustomErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}
