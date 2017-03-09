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

	private List<Criteria> criterias = new ArrayList<>();

	public BoSpecificationImpl(BoMeta boMeta, List<Criteria> criterias) throws BusinessException {
		this.criterias = criterias;
	}

	public static <T> BoSpecificationImpl<T> newInstance(Class<T> klass, BoMeta boMeta, List<Criteria> criterias)
			throws BusinessException {
		return new BoSpecificationImpl<T>(boMeta, criterias);
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) throws BusinessException {
		if (this.criterias == null || this.criterias.size() == 0) {
			return null;
		}

		List<Predicate> predicateList = new ArrayList<>();

		for (Criteria criteria : criterias) {
			predicateList.add(criteria.parse(root, query, cb));
		}

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
