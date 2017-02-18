package com.huoyun.core.bo.validate;

import com.huoyun.exception.BusinessException;

public interface Validator {

	void validator(Object value) throws BusinessException;
}
