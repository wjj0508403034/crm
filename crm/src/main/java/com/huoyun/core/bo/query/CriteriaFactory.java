package com.huoyun.core.bo.query;

import java.util.List;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.core.bo.query.criteria.OrderBy;
import com.huoyun.exception.BusinessException;

public interface CriteriaFactory {

	Criteria parse(BoMeta boMeta, String query) throws BusinessException;

	List<OrderBy> parseOrderBy(BoMeta boMeta, String orderby)
			throws BusinessException;
}
