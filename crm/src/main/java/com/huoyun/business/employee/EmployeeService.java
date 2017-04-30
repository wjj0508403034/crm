package com.huoyun.business.employee;

import com.huoyun.exception.BusinessException;

public interface EmployeeService {

	Employee getEmployeeByUserId(Long userId);
	
	Employee getEmployeeById(Long id);

	void changePasswordParam(ChangePasswordParam changePasswordParam) throws BusinessException;
}
