package com.huoyun.core.bo.ext;

import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.exception.BusinessException;

public interface ColumnAllocFactory {

	String alloc(PropertyType type) throws BusinessException;

	void append(PropertyType type,String columnName) throws BusinessException;
}
