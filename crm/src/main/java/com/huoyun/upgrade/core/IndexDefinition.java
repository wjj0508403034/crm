package com.huoyun.upgrade.core;

public class IndexDefinition {
	private String schemaName;

	private String indexName;

	private String tableName;

	private boolean unique;

	private IndexColumn[] columnNames;

	private boolean indexOrder;

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public IndexColumn[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(IndexColumn[] columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isIndexOrder() {
		return indexOrder;
	}

	public void setIndexOrder(boolean indexOrder) {
		this.indexOrder = indexOrder;
	}
}
