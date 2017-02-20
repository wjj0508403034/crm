package com.huoyun.upgrade.core;

import java.io.Serializable;

public class SequencesDefinition implements Serializable{
	private static final long serialVersionUID = -8029656814008742298L;

    private String schemaName;

    private String sequenceName;

    private String sequenceOid;

    private Integer startNumber;

    private Integer minValue;

    private Integer maxValue;

    private Integer incrementBy;

    private Boolean isCycled;

    private String resetQuery;

    private Integer cacheSize;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getSequenceOid() {
        return sequenceOid;
    }

    public void setSequenceOid(String sequenceOid) {
        this.sequenceOid = sequenceOid;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getIncrementBy() {
        return incrementBy;
    }

    public void setIncrementBy(Integer incrementBy) {
        this.incrementBy = incrementBy;
    }

    public Boolean getIsCycled() {
        return isCycled;
    }

    public void setIsCycled(Boolean isCycled) {
        this.isCycled = isCycled;
    }

    public String getResetQuery() {
        return resetQuery;
    }

    public void setResetQuery(String resetQuery) {
        this.resetQuery = resetQuery;
    }

    public Integer getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(Integer cacheSize) {
        this.cacheSize = cacheSize;
    }
}
