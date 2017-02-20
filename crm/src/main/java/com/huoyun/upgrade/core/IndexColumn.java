package com.huoyun.upgrade.core;

public class IndexColumn {
	private String columnName;

	private boolean indexOrder = true;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean isIndexOrder() {
		return indexOrder;
	}

	public void setIndexOrder(boolean indexOrder) {
		this.indexOrder = indexOrder;
	}
}
