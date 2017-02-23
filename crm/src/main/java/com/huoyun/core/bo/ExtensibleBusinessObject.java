package com.huoyun.core.bo;

import com.huoyun.core.bo.annotation.ColumnRule;
import com.huoyun.core.bo.annotation.ExtTable;
import com.huoyun.core.bo.ext.ExtUtils;

@ExtTable(columnRules = {
		@ColumnRule(count = ExtUtils.STR_COUNT, prefix = "STR", valueType = "VARCHAR(254)"),
		@ColumnRule(count = ExtUtils.NUM_COUNT, prefix = "NUM", valueType = "DECIMAL(25)"),
		@ColumnRule(count = ExtUtils.TXT_COUNT, prefix = "TXT", valueType = "TEXT") })
public interface ExtensibleBusinessObject {

}
