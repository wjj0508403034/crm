package com.huoyun.core.bo.ext.controller;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.metadata.Value;

public class CustomFieldParam {

	private String boName;
	private String boNamespace;
	private String name;
	private String label;
	private String type;
	private List<Value> validValues = new ArrayList<>();

	public String getBoName() {
		return boName;
	}

	public void setBoName(String boName) {
		this.boName = boName;
	}

	public String getBoNamespace() {
		return boNamespace;
	}

	public void setBoNamespace(String boNamespace) {
		this.boNamespace = boNamespace;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Value> getValidValues() {
		return validValues;
	}

	public void setValidValues(List<Value> validValues) {
		this.validValues = validValues;
	}

}
