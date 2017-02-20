package com.huoyun.upgrade.core;

import java.io.Serializable;

public class UniqueDefinition implements Serializable {
	private static final long serialVersionUID = 3367870776458531668L;

	private String tableName;

	private String constraintName;

	private UniqueColumn[] columnNames;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public UniqueColumn[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(UniqueColumn[] columnNames) {
		this.columnNames = columnNames;
	}
}
