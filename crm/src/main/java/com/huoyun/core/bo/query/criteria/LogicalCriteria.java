package com.huoyun.core.bo.query.criteria;

public abstract class LogicalCriteria implements Criteria {

	protected Criteria left;
	protected Criteria right;

	public LogicalCriteria(Criteria leftCriteria, Criteria rightCriteria) {
		this.left = leftCriteria;
		this.right = rightCriteria;
	}
}
