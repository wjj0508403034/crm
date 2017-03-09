package com.huoyun.core.bo.query;

import java.util.List;

import com.huoyun.exception.BusinessException;

public interface Parser {

	List<Token> getTokens() throws BusinessException;
}
