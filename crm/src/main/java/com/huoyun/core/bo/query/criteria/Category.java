package com.huoyun.core.bo.query.criteria;

public enum Category {
	Equal("=", Equal.class), NotEqual("<>", NotEqual.class), GreaterThan(">",
			GreaterThan.class), GreaterThanAndEqual(">=",
			GreaterThanAndEqual.class), LessThan("<", LessThan.class), LessThanAndEqual(
			"<=", LessThanAndEqual.class);

	private final String value;
	private final Class<?> klass;

	<T extends Criteria> Category(String v, Class<T> klass) {
		value = v;
		this.klass = klass;
	}

	public String value() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public <T extends Criteria> Class<T> getCriteriaType() {
		return (Class<T>) this.klass;
	}
}
