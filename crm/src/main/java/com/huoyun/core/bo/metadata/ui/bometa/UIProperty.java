package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
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
	private Map<String, Object> additionInfo = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		if(StringUtils.isEmpty(label)){
			return name;
		}
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

	public Map<String, Object> getAdditionInfo() {
		return additionInfo;
	}

	public void setAdditionInfo(Map<String, Object> additionInfo) {
		this.additionInfo = additionInfo;
	}

	public static UIProperty parse(PropertyMeta propMeta,BusinessObjectFacade boFacade) {
		UIProperty uiProp = new UIProperty();
		uiProp.setName(propMeta.getName());
		uiProp.setNamespace(propMeta.getNamespace());
		uiProp.setLabel(propMeta.getLabel());
		uiProp.setType(propMeta.getType());
		uiProp.setMandatory(propMeta.isMandatory());
		uiProp.setReadonly(propMeta.isReadonly());
		uiProp.setSearchable(propMeta.isSearchable());
		uiProp.setValidvalues(propMeta.getValidValues());
		uiProp.setAdditionInfo(propMeta.getAdditionInfo());
		if(propMeta.getType() == PropertyType.BoLabel){
			Class<?> targetBoType = propMeta.getRuntimeType();
			BoMeta bometa = boFacade.getMetadataRepository().getBoMeta(targetBoType);
			uiProp.additionInfo.put("boNamespace", bometa.getNamespace());
			uiProp.additionInfo.put("boName", bometa.getName());
			uiProp.additionInfo.put("idField", bometa.getPrimaryKey());
			uiProp.additionInfo.put("labelField", bometa.getBusinessKey());
		}
		return uiProp;
	}

}
