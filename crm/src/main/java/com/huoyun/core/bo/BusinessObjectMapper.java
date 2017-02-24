package com.huoyun.core.bo;

import java.util.Map;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.exception.BusinessException;

public interface BusinessObjectMapper {

	Map<String, Object> converterTo(BusinessObject bo, BoMeta boMeta) throws BusinessException;

}
