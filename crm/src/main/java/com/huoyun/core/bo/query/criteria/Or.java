package com.huoyun.core.bo.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.exception.BusinessException;

public class Or extends LogicalCriteria {

	public Or(List<Criteria> left, List<Criteria> right) {
		super(left, right);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
