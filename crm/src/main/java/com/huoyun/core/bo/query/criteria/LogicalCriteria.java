package com.huoyun.core.bo.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.exception.BusinessException;

public abstract class LogicalCriteria implements Criteria {

	protected final List<Criteria> left;
	protected final List<Criteria> right;

	public LogicalCriteria(List<Criteria> left, List<Criteria> right) {
		this.left = left;
		this.right = right;
	}

	protected Predicate getPredicateOfList(List<Criteria> criterias,
			Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			throws BusinessException {
		Predicate predicate = null;
		for (Criteria criteria : criterias) {
			Predicate temp = criteria.parse(root, query, cb);

			if (predicate == null) {
				predicate = temp;
			} else {
				predicate = cb.and(predicate, temp);
			}
		}

		return predicate;
	}
}
