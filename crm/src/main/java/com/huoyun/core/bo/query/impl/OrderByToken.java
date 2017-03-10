package com.huoyun.core.bo.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class OrderByToken extends AbstractToken {

	private String propertyName;
	private boolean asc;

	public OrderByToken(String expr) throws BusinessException {
		super(expr);

		List<String> tokens = new ArrayList<>();
		for (String item : expr.split(" ")) {
			if (!StringUtils.equalsIgnoreCase(item.trim(), "")) {
				tokens.add(item);
			}
		}

		if (tokens.size() == 1) {
			this.propertyName = tokens.get(0);
			this.asc = true;
		} else if (tokens.size() == 2) {
			this.propertyName = tokens.get(0);
			if (StringUtils.equalsIgnoreCase(tokens.get(1), "asc")) {
				this.asc = true;
			} else if (StringUtils.equalsIgnoreCase(tokens.get(1), "desc")) {
				this.asc = false;
			} else {
				throw new BusinessException(
						ErrorCode.Query_Expression_Parse_Failed);
			}
		} else {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public boolean isAsc() {
		return this.asc;
	}

}
