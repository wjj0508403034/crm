package com.huoyun.core.bo.query.parser;

import com.huoyun.core.bo.query.parser.impl.filters.Filter;

public interface ParserService {

	Filter parseFilter(String expr);
}
