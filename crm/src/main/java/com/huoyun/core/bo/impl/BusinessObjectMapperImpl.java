package com.huoyun.core.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> converterTo(BusinessObject bo, BoMeta boMeta)
			throws BusinessException {
		if (bo == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<>();
		for (PropertyMeta propMeta : boMeta.getProperties()) {
			String propertyName = propMeta.getName();
			Object propertyValue = bo.getPropertyValue(propertyName);
			if (propertyValue == null) {
				map.put(propertyName, null);
				continue;
			}

			if (propMeta.getNodeMeta() != null) {
				BoMeta nodeBoMeta = boMeta.getSubNodeBoMeta(
						this.boFacade.getMetadataRepository(), propertyName);
				map.put(propertyName, this.getNodePropertyValue(nodeBoMeta,
						(List<BusinessObject>) propertyValue));
			} else if (propMeta.getType() == PropertyType.BoLabel) {
				map.put(propertyName, this.getSimpleValueOfBo(propMeta,
						(BusinessObject) propertyValue));
			} else {
				map.put(propertyName, bo.getPropertyValue(propertyName));
			}

		}

		return map;
	}

	private List<Map<String, Object>> getNodePropertyValue(BoMeta nodeBoMeta,
			List<BusinessObject> boList) throws BusinessException {
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BusinessObject bo : boList) {
			mapList.add(this.converterTo(bo, nodeBoMeta));
		}
		return mapList;
	}

	private Map<String, Object> getBoValue(BoMeta boMeta, BusinessObject bo)
			throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		for (PropertyMeta propMeta : boMeta.getProperties()) {
			map.put(propMeta.getName(), bo.getPropertyValue(propMeta.getName()));
		}
		return map;
	}

	private Map<String, Object> getSimpleValueOfBo(PropertyMeta propMeta,
			BusinessObject bo) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		bo.setBoFacade(boFacade);
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(
				propMeta.getRuntimeType());
		String primaryKey = boMeta.getPrimaryKey();
		if (!StringUtils.isEmpty(primaryKey)) {
			map.put(primaryKey, bo.getPropertyValue(primaryKey));
		}

		String businessKey = boMeta.getBusinessKey();
		if (!StringUtils.isEmpty(businessKey)) {
			map.put(businessKey, bo.getPropertyValue(businessKey));
		}

		for (String displayField : boMeta.getDisplayFields()) {
			map.put(displayField, bo.getPropertyValue(displayField));
		}

		return map;
	}

}
