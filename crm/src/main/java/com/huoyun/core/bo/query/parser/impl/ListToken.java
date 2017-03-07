package com.huoyun.core.bo.query.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.query.parser.Token;

public class ListToken extends AbstractToken {

	public ListToken(String expr) {
		super(expr);

	}

	private List<Token> tokens = new ArrayList<>();

	public List<Token> getTokens() {
		return this.tokens;
	}
}
