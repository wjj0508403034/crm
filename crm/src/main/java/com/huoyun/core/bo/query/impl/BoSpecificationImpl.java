package com.huoyun.core.bo.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.BoSpecification;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.core.bo.query.criteria.OrderBy;
import com.huoyun.exception.BusinessException;

public class BoSpecificationImpl<T> implements BoSpecification<T> {

	private Criteria criteria = null;
	private List<OrderBy> orderbyList = null;

	public BoSpecificationImpl(BoMeta boMeta, Criteria criteria,
			List<OrderBy> orderbyList) throws BusinessException {
		this.criteria = criteria;
		this.orderbyList = orderbyList;
	}

	public static <T> BoSpecificationImpl<T> newInstance(Class<T> klass,
			BoMeta boMeta, Criteria criteria, List<OrderBy> orderbyList)
			throws BusinessException {
		return new BoSpecificationImpl<T>(boMeta, criteria, orderbyList);
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		if (this.criteria != null) {
			Predicate predicate = criteria.parse(root, query, cb);
			if (predicate != null) {
				query = query.where(predicate);
			}
		}

		if (this.orderbyList != null && this.orderbyList.size() > 0) {
			List<Order> orders = new ArrayList<>();
			for (OrderBy orderby : this.orderbyList) {
				orders.add(orderby.parse(root, query, cb));
			}
			query = query.orderBy(orders);
		}

		return query.getRestriction();
	}
}
