package com.huoyun.core.bo.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.BoSpecification;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.exception.BusinessException;

public class BoSpecificationImpl<T> implements BoSpecification<T> {

	private Criteria criteria = null;

	public BoSpecificationImpl(BoMeta boMeta, Criteria criteria) throws BusinessException {
		this.criteria = criteria;
	}

	public static <T> BoSpecificationImpl<T> newInstance(Class<T> klass, BoMeta boMeta, Criteria criteria)
			throws BusinessException {
		return new BoSpecificationImpl<T>(boMeta, criteria);
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) throws BusinessException {
		if (this.criteria == null) {
			return null;
		}

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(criteria.parse(root, query, cb));
//		for (Criteria criteria : criterias) {
//			
//		}

		if (predicateList.size() == 0) {
			return null;
		}

		return query.where(this.toArray(predicateList)).getRestriction();
	}

	private Predicate[] toArray(List<Predicate> predicateList) {
		Predicate[] array = new Predicate[predicateList.size()];
		predicateList.toArray(array);
		return array;
	}
}
