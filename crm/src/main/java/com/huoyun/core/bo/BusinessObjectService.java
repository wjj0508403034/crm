package com.huoyun.core.bo;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huoyun.exception.BusinessException;

public interface BusinessObjectService {

	Map<String, Object> initBo(String namespace, String name) throws BusinessException;

	Map<String, Object> createBo(String namespace, String name, Map<String, Object> data) throws BusinessException;

	Map<String, Object> load(String namespace, String name, Long id) throws BusinessException;

	void delete(String namespace, String name, Long id) throws BusinessException;

	Map<String, Object> updateBo(String namespace, String name, Long id, Map<String, Object> data)
			throws BusinessException;

	Page<Map<String, Object>> query(String namespace, String name, Pageable pageable, String query, String orderby)
			throws BusinessException;

	Long count(String namespace, String name, String query) throws BusinessException;

	void batchUpdate(String namespace, String name, List<Map<String, Object>> boList) throws BusinessException;

	List<Map<String, Object>> queryAll(String namespace, String name, String query, String orderby)
			throws BusinessException;
}
