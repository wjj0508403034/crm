package com.huoyun.login;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.employee.EmployeeService;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.multitenant.TenantContext;
import com.huoyun.exception.BusinessException;
import com.huoyun.session.Session;
import com.huoyun.session.impl.SessionImpl;

public class LoginProcessor {

	private BusinessObjectFacade boFacaemde;

	public LoginProcessor(BusinessObjectFacade boFacade) {
		this.boFacaemde = boFacade;
	}

	public Session process(Long userId,String tenantCode) throws BusinessException {
		TenantContext.setCurrentTenantCode(tenantCode);
		Employee employee = this.boFacaemde.getBean(EmployeeService.class)
				.getEmployeeByUserId(userId);
		if (employee == null) {
			throw new BusinessException(LoginErrorCodes.Employee_Not_Found);
		}
		
		this.boFacaemde.getMetadataRepository().refresh();

		return new SessionImpl(employee);
	}
}
