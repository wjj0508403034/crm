package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.List;

public class UIListView {

	private List<String> properties = new ArrayList<>();
	private String orderby;
	private Boolean enableSort;
	private String sortProperty;

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public Boolean getEnableSort() {
		return enableSort;
	}

	public void setEnableSort(Boolean enableSort) {
		this.enableSort = enableSort;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}
}
