package com.huoyun.core.bo.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class In extends ComparableCriteria implements CollectionCriteria{

	public In(PropertyMeta propMeta, Object value) {
		super(propMeta, value);
	}

	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) throws BusinessException {
		return this.getPathExpression(root).in((List<?>) this.getValue());
	}

}
