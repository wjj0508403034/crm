package com.huoyun.login;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.employee.EmployeeService;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;
import com.huoyun.session.Session;
import com.huoyun.session.impl.SessionImpl;

public class LoginProcessor {

	private BusinessObjectFacade boFacade;

	public LoginProcessor(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	public Session process(Long userId) throws BusinessException {

		Employee employee = this.boFacade.getBean(EmployeeService.class)
				.getEmployeeByUserId(userId);
		if (employee == null) {
			throw new BusinessException(LoginErrorCodes.Employee_Not_Found);
		}
		
		this.boFacade.getMetadataRepository().refresh();

		return new SessionImpl(employee);
	}
}
