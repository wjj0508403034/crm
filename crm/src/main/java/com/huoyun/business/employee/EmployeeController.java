package com.huoyun.business.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;

@Controller
public class EmployeeController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseBody
	public Employee getProfile() throws BusinessException {
		Employee employee = this.boFacade.getCurrentEmployee();
		if (employee == null) {
			throw new BusinessException(EmployeeErrorCodes.Employee_Not_Found);
		}
		return this.boFacade.getBean(EmployeeService.class).getEmployeeById(
				employee.getId());
	}
}
