package com.huoyun.core.bo.query.parser.impl;

import com.huoyun.core.bo.query.parser.ParserService;
import com.huoyun.core.bo.query.parser.impl.filters.Filter;

public class ParserServiceImpl implements ParserService {

	@Override
	public Filter parseFilter(String expr) {
		return new Filter(expr);
	}

}
