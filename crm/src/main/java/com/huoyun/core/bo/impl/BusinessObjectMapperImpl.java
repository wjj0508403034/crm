package com.huoyun.core.bo.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectMapper;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.exception.BusinessException;

public class BusinessObjectMapperImpl implements BusinessObjectMapper {

	private BusinessObjectFacade boFacade;

	public BusinessObjectMapperImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public Map<String, Object> converterTo(BusinessObject bo, BoMeta boMeta) throws BusinessException {
		if (bo == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			String propertyName = propMeta.getName();
			if (propMeta.getType() == PropertyType.BoLabel) {
				BusinessObject propertyValue = (BusinessObject) bo.getPropertyValue(propertyName);
				if (propertyValue == null) {
					map.put(propertyName, null);
				} else {
					map.put(propertyName, this.getSimpleValueOfBo(propMeta, propertyValue));
				}
			} else {
				map.put(propertyName, bo.getPropertyValue(propertyName));
			}

		}

		return map;
	}

	private Map<String, Object> getSimpleValueOfBo(PropertyMeta propMeta, BusinessObject bo) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		bo.setBoFacade(boFacade);
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(propMeta.getRuntimeType());
		String primaryKey = boMeta.getPrimaryKey();
		if (!StringUtils.isEmpty(primaryKey)) {
			map.put(primaryKey, bo.getPropertyValue(primaryKey));
		}

		String businessKey = boMeta.getBusinessKey();
		if (!StringUtils.isEmpty(businessKey)) {
			map.put(businessKey, bo.getPropertyValue(businessKey));
		}

		return map;
	}

}
