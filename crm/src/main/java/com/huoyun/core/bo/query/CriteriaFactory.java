package com.huoyun.core.bo.query;

import java.util.List;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.exception.BusinessException;

public interface CriteriaFactory {

	List<Criteria> parse(BoMeta boMeta, String query) throws BusinessException;
}
