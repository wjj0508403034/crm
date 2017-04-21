package com.huoyun.core.bo.ext.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.ext.UserProperty;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;

public class ExtBoMetaImpl implements BoMeta {

	private BoMeta baseBoMeta;
	private Map<String, PropertyMeta> extPropMap = new HashMap<>();

	public ExtBoMetaImpl(BoMeta baseBoMeta) {
		this.baseBoMeta = baseBoMeta;
		for (PropertyMeta propMeta : this.baseBoMeta.getProperties()) {
			this.extPropMap.put(propMeta.getName(), new ExtPropertyMetaImpl(propMeta));
		}
	}

	@JsonIgnore
	@Override
	public Class<BusinessObject> getBoType() {
		return this.baseBoMeta.getBoType();
	}

	@Override
	public List<PropertyMeta> getProperties() {
		return new ArrayList<>(this.extPropMap.values());
	}

	@Override
	public boolean hasProperty(String propertyName) {
		return this.extPropMap.containsKey(propertyName);
	}

	@Override
	public PropertyMeta getPropertyMeta(String propertyName) {
		if (this.extPropMap.containsKey(propertyName)) {
			return this.extPropMap.get(propertyName);
		}
		return null;
	}

	@JsonIgnore
	@Override
	public String getExtTableName() {
		return this.baseBoMeta.getExtTableName();
	}

	@Override
	public String getName() {
		return this.baseBoMeta.getName();
	}

	public void mergeProperty(UserProperty userProperty) {
		ExtPropertyMetaImpl propMeta = null;
		if (this.extPropMap.containsKey(userProperty.getName())) {
			propMeta = (ExtPropertyMetaImpl) this.extPropMap.get(userProperty.getName());
		} else {
			propMeta = new ExtPropertyMetaImpl();
			this.extPropMap.put(userProperty.getName(), propMeta);
		}
		propMeta.merge(userProperty);
	}

	@Override
	public boolean isAllowCustomized() {
		return true;
	}

	@Override
	public String getNamespace() {
		return this.baseBoMeta.getNamespace();
	}

	@Override
	public String getLabel() {
		return this.baseBoMeta.getLabel();
	}

	@Override
	public String getBusinessKey() {
		return this.baseBoMeta.getBusinessKey();
	}

	@Override
	public String getPrimaryKey() {
		return this.baseBoMeta.getPrimaryKey();
	}

}
