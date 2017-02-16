package com.huoyun.core.bo.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.huoyun.business.Contact;
import com.huoyun.core.bo.metadata.BoMeta;

public class BusinessObjectSpecification<T> implements Specification<T> {

	private BoMeta boMeta;
	private QueryParam queryParam;

	public BusinessObjectSpecification(BoMeta boMeta, QueryParam queryParam) {
		this.boMeta = boMeta;
		this.queryParam = queryParam;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {

		return null;
	}
}
