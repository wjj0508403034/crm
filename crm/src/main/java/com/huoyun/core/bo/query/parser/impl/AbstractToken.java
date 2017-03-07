package com.huoyun.core.bo.query.parser.impl;

import com.huoyun.core.bo.query.parser.Token;

public class AbstractToken implements Token {

	private String expr;

	public AbstractToken(String expr) {
		this.expr = expr;
	}

	@Override
	public String toString() {
		return this.expr;
	}

	@Override
	public String getExpr() {
		return this.expr;
	}
}
