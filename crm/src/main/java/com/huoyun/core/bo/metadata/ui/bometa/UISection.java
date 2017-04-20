package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.List;

public class UISection {

	private String label;
	private List<String> properties = new ArrayList<>();
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getProperties() {
		return properties;
	}
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}
	
}
