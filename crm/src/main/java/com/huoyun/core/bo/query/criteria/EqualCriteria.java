package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EqualCriteria implements QueryCriteria {

	private CriteriaExpr expr;

	public EqualCriteria(CriteriaExpr expr) {
		this.expr = expr;
	}

	@Override
	public Category getCategory() {
		return Category.Equal;
	}

	@Override
	public Predicate parse(Root<?> root, CriteriaQuery query, CriteriaBuilder cb) {
		return cb.equal(root.get(this.expr.getName()), this.expr.getValue());
	}

}
