package com.huoyun.business.employee;

public interface EmployeeService {

	Employee getEmployeeByUserId(Long userId);
	
	Employee getEmployeeById(Long id);
}
