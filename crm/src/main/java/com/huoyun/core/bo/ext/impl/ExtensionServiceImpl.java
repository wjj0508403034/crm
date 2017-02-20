package com.huoyun.core.bo.ext.impl;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicHelper;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.core.bo.ext.UserEntity;
import com.huoyun.core.bo.metadata.BoMeta;

public class ExtensionServiceImpl implements ExtensionService {

	private BusinessObjectFacade boFacade;

	public ExtensionServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public UserEntity createDynamicEntity(BoMeta boMeta) {
		if (StringUtils.isEmpty(boMeta.getExtTableName())) {
			return null;
		}

		DynamicHelper dynamicHelper = new JPADynamicHelper(
				this.boFacade.getEntityManagerFactory());
		DynamicType type = dynamicHelper.getType(boMeta.getExtTableName());
		DynamicEntity dynamicEntity = type.newDynamicEntity();
		return new UserEntityImpl(dynamicEntity, boMeta);
	}

}
