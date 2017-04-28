package com.huoyun.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.business.employee.EmployeeService;
import com.huoyun.business.employee.impl.EmployeeServiceImpl;
import com.huoyun.core.bo.BusinessObjectFacade;

@Configuration
public class BusinessAutoConfiguration {

	@Bean
	public EmployeeService employeeService(BusinessObjectFacade boFacade){
		return new EmployeeServiceImpl(boFacade);
	}
}
