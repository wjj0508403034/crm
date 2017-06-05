package com.huoyun.business.payment;

import com.huoyun.exception.BusinessException;

public interface PaymentService {

	void afterPayment(Payment payment) throws BusinessException;
}
