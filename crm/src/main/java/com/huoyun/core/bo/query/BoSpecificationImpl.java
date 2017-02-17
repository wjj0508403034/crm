package com.huoyun.core.bo.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.exception.BusinessException;

public class BoSpecificationImpl<T> implements BoSpecification<T> {

	private QueryParam queryParam;
	private List<Criteria> criterias = new ArrayList<>();

	public BoSpecificationImpl(BoMeta boMeta, QueryParam queryParam)
			throws BusinessException {
		this.queryParam = queryParam;
		if (queryParam != null && queryParam.getCriterias().size() != 0) {
			this.criterias = this.queryParam.parse(boMeta);
		}
	}

	public static <T> BoSpecificationImpl<T> newInstance(Class<T> klass,
			BoMeta boMeta, QueryParam queryParam) throws BusinessException {
		return new BoSpecificationImpl<T>(boMeta, queryParam);
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		if (this.criterias.size() == 0) {
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
