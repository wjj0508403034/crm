package com.huoyun.core.bo.metadata.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.ExtensibleBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.locale.LocaleService;

public class BoMetaImpl implements BoMeta {

	private String name;
	private String namespace = BusinessObjectUtils.SYSTEM_BO_NAMESPACE;
	private String label;
	private String labelI18nKey;
	private LocaleService localeService;
	// private List<PropertyMeta> properties = new ArrayList<>();
	private String primaryKey;
	private String businessKey;
	private Class<BusinessObject> boType;
	private Map<String, PropertyMeta> propMap = new HashMap<>();
	private String extTableName;
	private boolean allowCustomized;

	@SuppressWarnings("unchecked")
	public BoMetaImpl(Class<?> boClass, LocaleService localeService) {

		this.localeService = localeService;
		BoEntity annot = boClass.getAnnotation(BoEntity.class);
		if (annot == null) {
			throw new RuntimeException("No BoEntity annotation");
		}

		this.setBoType((Class<BusinessObject>) boClass);

		this.name = BusinessObjectUtils.getBoName(boClass);
		this.namespace = BusinessObjectUtils.getBoNamespace(boClass);
		if (StringUtils.isEmpty(annot.label())) {
			this.labelI18nKey = "bo.label." + this.name;
		}
		this.label = this.localeService.getMessage(this.labelI18nKey);
		this.allowCustomized = annot.allowCustomized();

		if (ExtensibleBusinessObject.class.isAssignableFrom(boClass)) {
			this.setExtTableName(BusinessObjectUtils.getExtTableName(boClass));
		}

		this.setProps(this.name, boClass);
	}

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
	public List<PropertyMeta> getProperties() {
		return new ArrayList<>(this.propMap.values());
	}

	private void setProps(String boName, Class<?> klass) {
		if (klass.getSuperclass() != null) {
			this.setProps(boName, klass.getSuperclass());
		}

		for (Field field : klass.getDeclaredFields()) {
			PropertyMetaImpl propMeta = null;
			BoProperty boProp = field.getAnnotation(BoProperty.class);
			if (boProp != null) {
				propMeta = new PropertyMetaImpl(boName, field, localeService);
				this.propMap.put(propMeta.getName(), propMeta);
				// this.properties.add(propMeta);

				Id idAnnot = field.getAnnotation(Id.class);
				if (idAnnot != null) {
					this.primaryKey = propMeta.getName();
					propMeta.setReadonly(true);
				}

				BusinessKey busKeyAnno = field.getAnnotation(BusinessKey.class);
				if (busKeyAnno != null) {
					this.businessKey = propMeta.getName();
				}
			}

		}
	}

	@Override
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@JsonIgnore
	@Override
	public Class<BusinessObject> getBoType() {
		return boType;
	}

	public void setBoType(Class<BusinessObject> boType) {
		this.boType = boType;
	}

	@Override
	public boolean hasProperty(String propertyName) {
		return this.propMap.containsKey(propertyName);
	}

	@Override
	public PropertyMeta getPropertyMeta(String propertyName) {
		if (this.propMap.containsKey(propertyName)) {
			return this.propMap.get(propertyName);
		}
		return null;
	}

	@JsonIgnore
	@Override
	public String getExtTableName() {
		return extTableName;
	}

	public void setExtTableName(String extTableName) {
		this.extTableName = extTableName;
	}

	@Override
	public boolean isAllowCustomized() {
		return allowCustomized;
	}

	public void setAllowCustomized(boolean allowCustomized) {
		this.allowCustomized = allowCustomized;
	}

	@Override
	public String getBusinessKey() {
		if (StringUtils.isEmpty(this.businessKey)) {
			return this.primaryKey;
		}
		return businessKey;
	}
}
