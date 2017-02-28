package com.huoyun.core.bo.metadata;

import com.huoyun.core.bo.validator.RuleType;

public interface ValidationMeta {

	RuleType getRuleType();
	
	String getExpr();
	
	String getErrorMessage();
}
