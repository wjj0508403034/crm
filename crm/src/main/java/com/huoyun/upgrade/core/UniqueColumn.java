package com.huoyun.upgrade.core;

import java.io.Serializable;

public class UniqueColumn implements Serializable {
	private static final long serialVersionUID = -7896060699209571967L;
	private String columnName;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
