package com.huoyun.core.bo.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectService;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.BoSpecificationImpl;
import com.huoyun.core.bo.query.QueryParam;
import com.huoyun.exception.BusinessException;

public class BusinessObjectServiceImpl implements BusinessObjectService {

	private BusinessObjectFacade boFacade;

	public BusinessObjectServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public BusinessObject initBo(String namespace, String name) throws BusinessException {
		this.getBoMeta(namespace, name);
		return this.boFacade.newBo(namespace, name);
	}

	@Transactional
	@Override
	public BusinessObject createBo(String namespace, String name,
			Map<String, Object> data) throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.newBo(namespace, name);
		for (PropertyMeta propMeta : boMeta.getProperties()) {
			bo.setPropertyValue(propMeta.getName(),
					data.get(propMeta.getName()));
		}
		bo.create();

		return bo;
	}

	@Override
	public BusinessObject load(String namespace, String name, Long id)
			throws BusinessException {
		this.getBoMeta(namespace, name);

		return this.boFacade.getBoRepository(namespace, name).load(id);
	}

	@Modifying
	@Transactional
	@Override
	public void delete(String namespace, String name, Long id)
			throws BusinessException {
		this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.getBoRepository(namespace, name)
				.load(id);
		if (bo == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		bo.delete();
	}

	@Modifying
	@Transactional
	@Override
	public BusinessObject updateBo(String namespace, String name, Long id,
			Map<String, Object> data) throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);

		BusinessObject bo = this.boFacade.getBoRepository(namespace, name)
				.load(id);
		if (bo == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			if (data.containsKey(propMeta.getName())) {
				bo.setPropertyValue(propMeta.getName(),
						data.get(propMeta.getName()));
			}
		}

		bo.update();

		return bo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page<BusinessObject> query(String namespace, String name,
			Pageable pageable, QueryParam queryParam) throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);
		BoSpecificationImpl spec = BoSpecificationImpl.newInstance(boMeta.getBoType(),
				boMeta, queryParam);
		return this.boFacade.getBoRepository(namespace, name).query(spec,
				pageable);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Long count(String namespace, String name, QueryParam queryParam)
			throws BusinessException {
		BoMeta boMeta = this.getBoMeta(namespace, name);
		BoSpecificationImpl spec = BoSpecificationImpl.newInstance(boMeta.getBoType(),
				boMeta, queryParam);
		return this.boFacade.getBoRepository(namespace, name).count(spec);
	}

	private BoMeta getBoMeta(String namespace, String name)
			throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(
				namespace, name);
		if (boMeta == null) {
			throw new BusinessException(BoErrorCode.Unkown_Bo_Entity);
		}

		return boMeta;
	}

}
