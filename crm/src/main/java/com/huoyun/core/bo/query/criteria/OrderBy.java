package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;

public class OrderBy {

	private PropertyMeta propMeta;
	private boolean asc = true;

	public OrderBy(PropertyMeta propMeta, boolean asc) {
		this.propMeta = propMeta;
		this.asc = asc;
	}

	public Order parse(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (this.asc) {
			return cb.asc(root.get(this.propMeta.getName()).as(
					this.propMeta.getRuntimeType()));
		}

		return cb.desc(root.get(this.propMeta.getName()).as(
				this.propMeta.getRuntimeType()));
	}
}
