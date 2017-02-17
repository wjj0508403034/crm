package com.huoyun.core.bo.query.criteria;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.QueryExpressUtils;
import com.huoyun.exception.BusinessException;

public abstract class AbstractCriteria implements Criteria {

	protected final PropertyMeta propMeta;
	protected final CriteriaExpr expr;
	protected final ObjectMapper mapper = new ObjectMapper();

	public AbstractCriteria(PropertyMeta propMeta, CriteriaExpr expr) {
		this.propMeta = propMeta;
		this.expr = expr;
	}

	protected Expression<?> getPathExpression(Root<?> root) {
		return root.get(this.expr.getName()).as(this.propMeta.getRuntimeType());
	}

	protected Object parseValue() throws BusinessException {
		Object value = null;
		if (this.propMeta.getRuntimeType() == DateTime.class) {
			value = QueryExpressUtils.parseDate(this.expr.getValue());
		} else {
			value = this.expr.getValue();
		}

		return value;
	}
}
