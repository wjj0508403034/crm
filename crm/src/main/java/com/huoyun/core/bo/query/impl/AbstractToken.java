package com.huoyun.core.bo.query.impl;

import com.huoyun.core.bo.query.Token;

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

	@Override
	public void setExpr(String expr) {
		this.expr = expr;
	}
}
