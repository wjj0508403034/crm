package com.huoyun.core.bo.impl;

import java.util.HashMap;
import java.util.Map;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectMapper;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class BusinessObjectMapperImpl implements BusinessObjectMapper {

	@Override
	public Map<String, Object> converterTo(BusinessObject bo, BoMeta boMeta)
			throws BusinessException {
		if (bo == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			String propertyName = propMeta.getName();
			map.put(propertyName, bo.getPropertyValue(propertyName));
		}

		return map;
	}

}
