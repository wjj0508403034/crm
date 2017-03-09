package com.huoyun.core.bo.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.query.Token;

public class ListToken extends AbstractToken {

	public ListToken(String expr) {
		super(expr);

	}

	private List<Token> tokens = new ArrayList<>();

	public List<Token> getTokens() {
		return this.tokens;
	}
}
