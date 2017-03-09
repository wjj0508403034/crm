package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class LessThan extends ComparableCriteria {

	public LessThan(PropertyMeta propMeta, Object value) {
		super(propMeta, value);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		return cb.lessThan(
				(Expression<Comparable>) this.getPathExpression(root),
				(Comparable) this.getValue());
	}

}
