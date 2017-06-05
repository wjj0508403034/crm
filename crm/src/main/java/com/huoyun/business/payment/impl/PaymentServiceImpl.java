package com.huoyun.business.payment.impl;

import com.huoyun.business.contract.Contract;
import com.huoyun.business.payment.Payment;
import com.huoyun.business.payment.PaymentService;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;

public class PaymentServiceImpl implements PaymentService {

	private BusinessObjectFacade boFacade;

	public PaymentServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public void afterPayment(Payment payment) throws BusinessException {
		Contract contract = this.boFacade.getBoRepository(Contract.class).load(payment.getContract().getId());
		contract.update();
	}

}
