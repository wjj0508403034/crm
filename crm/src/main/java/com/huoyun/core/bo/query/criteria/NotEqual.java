package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class NotEqual extends AbstractCriteria {

	public NotEqual(PropertyMeta propMeta, CriteriaExpr expr) {
		super(propMeta, expr);
	}

	@Override
	public Category getCategory() {
		return Category.NotEqual;
	}

	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		return cb.notEqual(this.getPathExpression(root), this.parseValue());
	}

}
