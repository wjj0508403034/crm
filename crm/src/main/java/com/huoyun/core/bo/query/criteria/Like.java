package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class Like extends ComparableCriteria {

	public Like(PropertyMeta propMeta, Object value) throws BusinessException {
		super(propMeta, value);

		if (this.propMeta.getRuntimeType() != String.class) {
			throw new BusinessException(
					ErrorCode.Property_Not_Sopport_Like_Query);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Predicate parse(Root<?> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) throws BusinessException {
		String pattern = (String) this.getValue();
		pattern = "%" + pattern + "%";
		return cb.like((Expression<String>) this.getPathExpression(root),
				pattern);
	}

}
