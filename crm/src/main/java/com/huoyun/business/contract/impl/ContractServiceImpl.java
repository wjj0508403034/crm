package com.huoyun.business.contract.impl;

import java.math.BigDecimal;

import javax.persistence.TypedQuery;

import com.huoyun.business.contract.Contract;
import com.huoyun.business.contract.ContractService;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.utils.BigDecimalUtils;

public class ContractServiceImpl implements ContractService {

	private BusinessObjectFacade boFacade;

	public ContractServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public void beforeAmountChanged(Contract contract) {
		String sql = "select sum(t.amount) from Payment t where t.contract = :contract";
		TypedQuery<BigDecimal> query = this.boFacade.getEntityManager().createQuery(sql, BigDecimal.class);
		query.setParameter("contract", contract);
		BigDecimal payedAmount = query.getSingleResult();
		BigDecimal unpayedAmount = BigDecimalUtils.subtract(contract.getAmount(), payedAmount);
		contract.setPayedAmount(payedAmount);
		contract.setUnpayAmount(unpayedAmount);
	}

}
