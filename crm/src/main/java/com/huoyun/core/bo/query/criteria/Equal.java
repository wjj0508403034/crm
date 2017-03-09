package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class Equal extends ComparableCriteria {

	public Equal(PropertyMeta propMeta, Object value) {
		super(propMeta, value);
	}

	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		return cb.equal(this.getPathExpression(root), this.getValue());
	}
}
