package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;

public abstract class ComparableCriteria implements Criteria {

	protected final PropertyMeta propMeta;
	private Object value;

	public ComparableCriteria(PropertyMeta propMeta, Object value) {
		this.propMeta = propMeta;
		this.value = value;
	}
	
	protected Object getValue(){
		return this.value;
	}

	protected Expression<?> getPathExpression(Root<?> root) {
		return root.get(this.propMeta.getName()).as(this.propMeta.getRuntimeType());
	}
}
