package com.huoyun.core.bo.query;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.criteria.Category;
import com.huoyun.core.bo.query.criteria.CriteriaExpr;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.exception.BusinessException;

public class QueryParam {

	private List<CriteriaExpr> criterias = new ArrayList<>();

	public List<CriteriaExpr> getCriterias() {
		return criterias;
	}

	public void setCriterias(List<CriteriaExpr> criterias) {
		this.criterias = criterias;
	}

	public List<Criteria> parse(BoMeta boMeta) throws BusinessException {
		List<Criteria> list = new ArrayList<>();

		for (CriteriaExpr expr : this.criterias) {
			PropertyMeta propMeta = boMeta.getPropertyMeta(expr.getName());
			if (propMeta == null) {
				throw new BusinessException(BoErrorCode.Bo_Property_Not_Exist);
			}

			Category find = null;
			for (Category category : Category.values()) {
				if (StringUtils.equals(category.value(), expr.getOp())) {
					find = category;
					break;
				}
			}

			if (find == null) {
				throw new BusinessException(
						BoErrorCode.Bo_Query_Express_Parse_Failed);
			}

			try {
				Constructor<Criteria> constructor = find.getCriteriaType()
						.getConstructor(PropertyMeta.class, CriteriaExpr.class);
				list.add(constructor.newInstance(propMeta, expr));
			} catch (Exception e) {
				throw new BusinessException(
						BoErrorCode.Bo_Query_Express_Parse_Failed);
			}
		}

		return list;
	}
}
