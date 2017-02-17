package com.huoyun.core.bo;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huoyun.core.bo.query.QueryParam;
import com.huoyun.exception.BusinessException;

public interface BusinessObjectService {

	BusinessObject initBo(String namespace, String name)
			throws BusinessException;

	BusinessObject createBo(String namespace, String name,
			Map<String, Object> data) throws BusinessException;

	BusinessObject load(String namespace, String name, Long id)
			throws BusinessException;

	void delete(String namespace, String name, Long id)
			throws BusinessException;

	BusinessObject updateBo(String namespace, String name, Long id,
			Map<String, Object> data) throws BusinessException;

	Long count(String namespace, String name, QueryParam queryParam)
			throws BusinessException;

	Page<BusinessObject> query(String namespace, String name,
			Pageable pageable, QueryParam queryParam) throws BusinessException;
}
