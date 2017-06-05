package com.huoyun.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.business.contract.ContractService;
import com.huoyun.business.contract.impl.ContractServiceImpl;
import com.huoyun.business.customer.trace.TraceRecordService;
import com.huoyun.business.customer.trace.impl.TraceRecordServiceImpl;
import com.huoyun.business.employee.EmployeeService;
import com.huoyun.business.employee.impl.EmployeeServiceImpl;
import com.huoyun.business.leads.LeadsService;
import com.huoyun.business.leads.impl.LeadsServiceImpl;
import com.huoyun.business.payment.PaymentService;
import com.huoyun.business.payment.impl.PaymentServiceImpl;
import com.huoyun.core.bo.BusinessObjectFacade;

@Configuration
public class BusinessAutoConfiguration {

	@Bean
	public EmployeeService employeeService(BusinessObjectFacade boFacade) {
		return new EmployeeServiceImpl(boFacade);
	}

	@Bean
	public TraceRecordService traceRecordService(BusinessObjectFacade boFacade) {
		return new TraceRecordServiceImpl(boFacade);
	}

	@Bean
	public LeadsService leadsService(BusinessObjectFacade boFacade) {
		return new LeadsServiceImpl(boFacade);
	}
	
	@Bean
	public PaymentService paymentService(BusinessObjectFacade boFacade){
		return new PaymentServiceImpl(boFacade);
	}
	
	@Bean
	public ContractService contractService(BusinessObjectFacade boFacade){
		return new ContractServiceImpl(boFacade);
	}
}
