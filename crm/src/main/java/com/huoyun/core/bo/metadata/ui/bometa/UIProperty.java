package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.metadata.Value;

public class UIProperty {

	private String name;
	private String namespace;
	private String label;
	private PropertyType type;
	private boolean mandatory;
	private boolean readonly;
	private boolean searchable;
	private List<Value> validvalues = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PropertyType getType() {
		return type;
	}

	public void setType(PropertyType type) {
		this.type = type;
	}


	public List<Value> getValidvalues() {
		return validvalues;
	}

	public void setValidvalues(List<Value> validvalues) {
		this.validvalues = validvalues;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

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

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public static UIProperty parse(PropertyMeta propMeta) {
		UIProperty uiProp = new UIProperty();
		uiProp.setName(propMeta.getName());
		uiProp.setNamespace(propMeta.getNamespace());
		uiProp.setLabel(propMeta.getLabel());
		uiProp.setType(propMeta.getType());
		uiProp.setMandatory(propMeta.isMandatory());
		uiProp.setReadonly(propMeta.isReadonly());
		uiProp.setSearchable(propMeta.isSearchable());
		uiProp.setValidvalues(propMeta.getValidValues());
		return uiProp;
	}

}
