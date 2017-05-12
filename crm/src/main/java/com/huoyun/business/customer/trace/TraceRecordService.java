package com.huoyun.business.customer.trace;

import java.util.List;

import com.huoyun.exception.BusinessException;

public interface TraceRecordService {

	void postComment(Long customerId, String comment) throws BusinessException;

	List<CustomerTraceRecord> getComments(Long customerId) throws BusinessException;
}
