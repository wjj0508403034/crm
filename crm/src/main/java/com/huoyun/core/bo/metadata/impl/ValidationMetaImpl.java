package com.huoyun.core.bo.metadata.impl;

import com.huoyun.core.bo.annotation.BoPropertyRule;
import com.huoyun.core.bo.metadata.ValidationMeta;
import com.huoyun.core.bo.validator.RuleType;

public class ValidationMetaImpl implements ValidationMeta {

	private RuleType ruleType;
	private String expr;
	private String errorMessage;

	public ValidationMetaImpl() {

	}

	public ValidationMetaImpl(BoPropertyRule boPropertyRule) {
		this.ruleType = boPropertyRule.rule();
		this.expr = boPropertyRule.expr();
	}

	@Override
	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

	@Override
	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
