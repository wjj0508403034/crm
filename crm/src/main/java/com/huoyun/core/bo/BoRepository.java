package com.huoyun.core.bo;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huoyun.core.bo.query.BoSpecification;
import com.huoyun.exception.BusinessException;

public interface BoRepository<T extends BusinessObject> {

	T load(Long id);

	void save(T bo);

	void update(T bo);

	void delete(T bo);

	void flush();

	Long count(BoSpecification<T> spec) throws BusinessException;

	Page<T> query(BoSpecification<T> spec, Pageable pageable)
			throws BusinessException;

	List<T> queryForList();

	TypedQuery<Long> newCountQuery(String sql);

	TypedQuery<T> newQuery(String sql);

	List<T> queryAll(BoSpecification<T> spec) throws BusinessException;

	Object sum(String propertyName, BoSpecification<T> spec)
			throws BusinessException;
}
