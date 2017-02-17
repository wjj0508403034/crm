package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.exception.BusinessException;

public interface Criteria {

	Category getCategory();
	
	Predicate parse(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) throws BusinessException;
}
