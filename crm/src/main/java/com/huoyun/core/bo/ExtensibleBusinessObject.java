package com.huoyun.core.bo;

import com.huoyun.core.bo.annotation.ColumnRule;

import com.huoyun.core.bo.annotation.ExtTable;

@ExtTable(columnRules = {
		@ColumnRule(count = 72, prefix = "STR", valueType = "VARCHAR(254)"),
		@ColumnRule(count = 60, prefix = "NUM", valueType = "DECIMAL(25)"),
		@ColumnRule(count = 6, prefix = "TXT", valueType = "TEXT") })
public interface ExtensibleBusinessObject {

}
