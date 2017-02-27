package com.huoyun.core.bo.ext.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicHelper;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.util.CollectionUtils;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.ExtensibleBusinessObject;
import com.huoyun.core.bo.ext.ColumnAllocFactory;
import com.huoyun.core.bo.ext.ExtErrorCode;
import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.core.bo.ext.UDEAllocInfo;
import com.huoyun.core.bo.ext.UserEntity;
import com.huoyun.core.bo.ext.UserProperty;
import com.huoyun.core.bo.ext.controller.CustomFieldParam;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.metadata.events.MetadataChangedPublisher;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.exception.BusinessException;

public class ExtensionServiceImpl implements ExtensionService {

	private BusinessObjectFacade boFacade;
	private MetadataChangedPublisher metaChangedPublisher;

	public ExtensionServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public void setMetadataChangedPublisher(
			MetadataChangedPublisher metadataChangedPublisher) {
		this.metaChangedPublisher = metadataChangedPublisher;
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

	@Modifying
	@Transactional
	@Override
	public void createUDF(CustomFieldParam customFieldParam)
			throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository()
				.getBoMeta(customFieldParam.getBoNamespace(),
						customFieldParam.getBoName());
		if (boMeta == null) {
			throw new BusinessException(BoErrorCode.Unkown_Bo_Entity);
		}

		PropertyType propType = PropertyType.parse(customFieldParam.getType());

		String sql = "select count(t) from UserProperty t where t.boNamespace = :boNamespace and t.boName = :boName and t.name = :name";
		TypedQuery<Long> query = this.boFacade.getBoRepository(
				UserProperty.class).newCountQuery(sql);
		query.setParameter("boNamespace", customFieldParam.getBoNamespace());
		query.setParameter("boName", customFieldParam.getBoName());
		query.setParameter("name", customFieldParam.getName());
		Long result = query.getSingleResult();
		if (result > 0) {
			throw new BusinessException(ExtErrorCode.UDF_EXIST);
		}
		String columnName = this.allocColumnForUserProperty(
				boMeta.getExtTableName(), propType);

		UserProperty userProperty = this.boFacade.newBo(UserProperty.class);
		userProperty.setBoNamespace(customFieldParam.getBoNamespace());
		userProperty.setBoName(customFieldParam.getBoName());
		userProperty.setName(customFieldParam.getName());
		userProperty.setLabel(customFieldParam.getLabel());
		userProperty.setTableName(boMeta.getExtTableName());
		userProperty.setColumnName(columnName);
		userProperty.setType(propType);
		userProperty.create();

		if (this.metaChangedPublisher != null) {
			this.metaChangedPublisher.publish();
		}
	}

	private String allocColumnForUserProperty(String tableName,
			PropertyType propertyType) throws BusinessException {
		UDEAllocInfo allocInfo = null;
		String sql = "select t from UDEAllocInfo t where t.tableName = :tableName";
		TypedQuery<UDEAllocInfo> query = this.boFacade.getBoRepository(
				UDEAllocInfo.class).newQuery(sql);
		query.setParameter("tableName", tableName);
		List<UDEAllocInfo> allocInfos = query.getResultList();
		if (allocInfos.size() > 0) {
			allocInfo = allocInfos.get(0);
			allocInfo.setBoFacade(this.boFacade);
		} else {
			allocInfo = this.boFacade.newBo(UDEAllocInfo.class);
			allocInfo.setTableName(tableName);
			allocInfo.create();
		}

		ColumnAllocFactory allocFactory = new ColumnAllocFactoryImpl(allocInfo);
		String columnName = allocFactory.alloc(propertyType);
		allocFactory.append(propertyType, columnName);
		allocInfo.update();
		return columnName;
	}

}
