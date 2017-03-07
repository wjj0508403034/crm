package com.huoyun.core.bo.query.criteria;

import java.util.List;

public abstract class LogicalCriteria implements Criteria {

	private List<Criteria> left;
	private List<Criteria> right;

	public LogicalCriteria(List<Criteria> left, List<Criteria> right) {
		this.left = left;
		this.right = right;
	}

}
