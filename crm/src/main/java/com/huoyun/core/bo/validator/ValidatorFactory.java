package com.huoyun.core.bo.validator;

import java.util.List;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public interface ValidatorFactory {

	List<Validator> getValidators(PropertyMeta propMeta, Object propertyValue) throws BusinessException;
}
