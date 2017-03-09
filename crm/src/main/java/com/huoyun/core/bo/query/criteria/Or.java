package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.exception.BusinessException;

public class Or extends LogicalCriteria {

	public Or(Criteria leftCriteria, Criteria rightCriteria) {
		super(leftCriteria, rightCriteria);
	}

	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) throws BusinessException {
		Predicate leftPredicate = this.left.parse(root, query, cb);
		Predicate rightPredicate = this.right.parse(root, query, cb);
		return cb.or(leftPredicate, rightPredicate);
	}

}
