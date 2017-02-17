package com.huoyun.core.bo.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.exception.BusinessException;

public interface BoSpecification<T> {

	Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException;
}
