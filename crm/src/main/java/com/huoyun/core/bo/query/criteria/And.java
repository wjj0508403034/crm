package com.huoyun.core.bo.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.exception.BusinessException;

public class And extends LogicalCriteria {

	public And(List<Criteria> left, List<Criteria> right) {
		super(left, right);
	}


	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		Predicate leftPredicate = this.getPredicateOfList(this.left, root,
				query, cb);
		Predicate rightPredicate = this.getPredicateOfList(this.right, root,
				query, cb);
		return cb.and(leftPredicate, rightPredicate);
	}
}
