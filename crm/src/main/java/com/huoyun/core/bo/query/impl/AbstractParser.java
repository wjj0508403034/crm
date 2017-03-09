package com.huoyun.core.bo.query.impl;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.query.Parser;

public abstract class AbstractParser implements Parser {

	private String expr;

	public AbstractParser(String expr) {
		if (!StringUtils.isEmpty(expr)) {
			this.expr = expr.trim();
		}
	}

	public final String getExpr() {
		return this.expr;
	}

}
