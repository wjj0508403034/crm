package com.huoyun.core.bo.query;

import com.huoyun.exception.BusinessException;

public interface ValueConverter {

	Object converter(String value) throws BusinessException;
}
