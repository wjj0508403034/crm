package com.huoyun.business.leads.impl;

import com.huoyun.business.customer.Customer;
import com.huoyun.business.leads.Leads;
import com.huoyun.business.leads.LeadsService;
import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;

public class LeadsServiceImpl implements LeadsService {

	private BusinessObjectFacade boFacade;

	public LeadsServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public Customer generateToCustomer(Long leadsId) throws BusinessException {
		Leads leads = this.boFacade.getBoRepository(Leads.class).load(leadsId);
		if (leads == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}
		
		Customer customer = this.boFacade.newBo(Customer.class);
		customer.setName(leads.getName());
		customer.setSalesSource(leads.getSalesSource());
		customer.setHouses(leads.getHouses());
		customer.setTelephone(leads.getTelephone());
		return customer;
	}

}
