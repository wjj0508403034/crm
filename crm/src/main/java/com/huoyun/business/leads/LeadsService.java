package com.huoyun.business.leads;

import com.huoyun.business.customer.Customer;
import com.huoyun.exception.BusinessException;

public interface LeadsService {

	Customer generateToCustomer(Long leadsId) throws BusinessException;
}
