package com.huoyun.core.bo.query;

import java.util.List;

import com.huoyun.exception.BusinessException;

public interface ValueConverter {

	Object converter(String value) throws BusinessException;

	List<Object> converterToList(String value) throws BusinessException;
}
