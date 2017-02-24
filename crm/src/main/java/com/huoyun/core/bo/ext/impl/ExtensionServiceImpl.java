package com.huoyun.core.bo.ext.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicHelper;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.springframework.util.CollectionUtils;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.ExtensibleBusinessObject;
import com.huoyun.core.bo.ext.ExtErrorCode;
import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.core.bo.ext.UserEntity;
import com.huoyun.core.bo.ext.UserProperty;
import com.huoyun.core.bo.ext.controller.CustomFieldParam;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.exception.BusinessException;

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
				this.boFacade.getEntityManager());
		DynamicType type = dynamicHelper.getType(boMeta.getExtTableName());
		DynamicEntity dynamicEntity = type.newDynamicEntity();
		return new UserEntityImpl(dynamicEntity, boMeta);
	}

	@Override
	public UserEntity load(BusinessObject bo, BoMeta boMeta) {
		if (bo == null || bo.getId() == null) {
			return null;
		}

		if (bo instanceof ExtensibleBusinessObject) {
			DynamicHelper dynamicHelper = new JPADynamicHelper(
					this.boFacade.getEntityManager());
			DynamicType type = dynamicHelper.getType(boMeta.getExtTableName());
			String sql = String
					.format("SELECT t FROM %s t WHERE t.%s = ?1",
							boMeta.getExtTableName(),
							BusinessObjectUtils.EXT_TABLE_PID);
			TypedQuery<? extends DynamicEntity> query = this.boFacade
					.getEntityManager().createQuery(sql, type.getJavaClass());
			query.setHint(QueryHints.FLUSH, HintValues.FALSE);
			query.setParameter(1, bo.getId());
			List<? extends DynamicEntity> results = query.getResultList();
			if (!CollectionUtils.isEmpty(results)) {
				return new UserEntityImpl(results.get(0), boMeta);
			}

		}
		return null;
	}

	@Override
	public void persist(BusinessObject bo) {
		if (bo == null || bo.getId() == null) {
			return;
		}

		if (bo instanceof ExtensibleBusinessObject) {
			UserEntity userEntity = ((ExtensibleBusinessObject) bo)
					.getUserEntity();
			if (userEntity != null) {
				userEntity.setParentId(bo.getId());
				userEntity.persist(this.boFacade.getEntityManager());
			}
		}

	}

	@Override
	public void createUDF(CustomFieldParam customFieldParam) throws BusinessException {
		String sql = "select t from UserProperty t where t.boNamespace = :boNamespace and t.boName = :boName and t.name = :name";
		TypedQuery<UserProperty> query = this.boFacade.getBoRepository(
				UserProperty.class).newQuery(sql);
		query.setParameter("boNamespace", customFieldParam.getBoNamespace());
		query.setParameter("boName", customFieldParam.getBoName());
		query.setParameter("name", customFieldParam.getName());
		List<UserProperty> userProperties = query.getResultList();
		if (userProperties.size() > 0) {
			throw new BusinessException(ExtErrorCode.UDF_EXIST);
		}
	}

}
