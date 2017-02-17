package com.huoyun.core.bo.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.criteria.QueryCriteria;

public class BoSpecification<T> implements Specification<T> {

	private BoMeta boMeta;
	private QueryParam queryParam;

	public BoSpecification(BoMeta boMeta, QueryParam queryParam) {
		this.boMeta = boMeta;
		this.queryParam = queryParam;
	}

	public static <T> BoSpecification<T> newInstance(Class<T> klass,
			BoMeta boMeta, QueryParam queryParam) {
		return new BoSpecification<T>(boMeta, queryParam);
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		if (queryParam == null || queryParam.getCriterias().size() == 0) {
			return null;
		}

		List<Predicate> predicateList = new ArrayList<>();
		List<QueryCriteria> criterias = this.queryParam.parse();
		for (QueryCriteria criteria : criterias) {
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
