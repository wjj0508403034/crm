package com.huoyun.business.customer.trace.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.huoyun.business.customer.Customer;
import com.huoyun.business.customer.trace.CustomerTraceRecord;
import com.huoyun.business.customer.trace.TraceRecordService;
import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;

public class TraceRecordServiceImpl implements TraceRecordService {

	private BusinessObjectFacade boFacade;

	public TraceRecordServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Transactional
	@Override
	public void postComment(Long customerId, String comment) throws BusinessException {
		Customer customer = this.boFacade.getBoRepository(Customer.class).load(customerId);
		if (customer == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		CustomerTraceRecord record = this.boFacade.newBo(CustomerTraceRecord.class);
		record.setComment(comment);
		record.setCustomer(customer);
		record.setOwner(this.boFacade.getCurrentEmployee());
		record.create();
	}

	@Override
	public List<CustomerTraceRecord> getComments(Long customerId) throws BusinessException {
		Customer customer = this.boFacade.getBoRepository(Customer.class).load(customerId);
		if (customer == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		String sql = "select t from CustomerTraceRecord t where t.customer = :customer";
		TypedQuery<CustomerTraceRecord> query = this.boFacade.getBoRepository(CustomerTraceRecord.class).newQuery(sql);
		query.setParameter("customer", customer);
		return query.getResultList();
	}

}
