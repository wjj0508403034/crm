package com.huoyun.core.bo.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class Between extends ComparableCriteria implements CollectionCriteria {

	private Object minValue;
	private Object maxValue;

	public Between(PropertyMeta propMeta, Object value)
			throws BusinessException {
		super(propMeta, value);
		List<?> list = (List<?>) value;
		if (list.size() != 2) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		this.minValue = list.get(0);
		this.maxValue = list.get(1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {

		return cb.between(
				(Expression<Comparable>) this.getPathExpression(root),
				(Comparable) this.minValue, (Comparable) this.maxValue);
	}

}
