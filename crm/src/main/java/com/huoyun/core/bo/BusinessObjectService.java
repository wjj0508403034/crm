package com.huoyun.core.bo;

import java.util.List;
import java.util.Map;

import com.huoyun.exception.BusinessException;

public interface BusinessObjectService {

	BusinessObject initBo(String namespace, String name);

	BusinessObject createBo(String namespace, String name,
			Map<String, Object> data) throws BusinessException;

	BusinessObject load(String namespace, String name, Long id)
			throws BusinessException;

	void delete(String namespace, String name, Long id)
			throws BusinessException;

	BusinessObject updateBo(String namespace, String name, Long id,
			Map<String, Object> data) throws BusinessException;

	List<BusinessObject> query(String namespace, String name)
			throws BusinessException;
}
