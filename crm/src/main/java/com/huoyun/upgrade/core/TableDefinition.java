package com.huoyun.upgrade.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TableDefinition implements Serializable{
	private static final long serialVersionUID = -3385980013134910756L;

    private String name;
    
    private String schema;

    private String tableDescription;

    private ColumnHashMap<String, Column> columns = new ColumnHashMap<String, Column>();

    private List<KEYColumn> keys = new LinkedList<KEYColumn>();

    private SequencesDefinition seq;

    private List<IndexDefinition> index = new LinkedList<>();

    private List<UniqueDefinition> unique = new LinkedList<>();


    public List<UniqueDefinition> getUnique() {
        if (null == unique) {
            unique = new LinkedList<>();
        }
        return unique;
    }


    public void setUnique(List<UniqueDefinition> unique) {
        this.unique = unique;
    }

    public List<IndexDefinition> getIndex() {
        return index;
    }

    public void setIndex(List<IndexDefinition> index) {
        this.index = index;
    }

    public SequencesDefinition getSeq() {
        return seq;
    }

    public void setSeq(SequencesDefinition seq) {
        this.seq = seq;
    }

    public List<KEYColumn> getKeys() {
        return keys;
    }

    public void setKeys(List<KEYColumn> keys) {
        this.keys = keys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableDescription() {
        return tableDescription;
    }

    public void setTableDescription(String tableDescription) {
        this.tableDescription = tableDescription;
    }

    public ColumnHashMap<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(ColumnHashMap<String, Column> columns) {
        this.columns = columns;
    }

    public List<KEYColumn> getPK() {
        List<KEYColumn> keys = new LinkedList<KEYColumn>();
        for (KEYColumn key : this.keys) {
            if (key.getKeyType() == KEYType.PK)
                keys.add(key);
        }
        return keys;
    }

    public List<KEYColumn> getFK() {
        List<KEYColumn> keys = new LinkedList<KEYColumn>();
        for (KEYColumn key : this.keys) {
            if (key.getKeyType() == KEYType.FK)
                keys.add(key);
        }
        return keys;
    }


	public String getSchema() {
		return schema;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}
}
