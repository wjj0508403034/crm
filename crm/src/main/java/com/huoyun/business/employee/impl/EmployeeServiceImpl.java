package com.huoyun.business.employee.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.business.employee.ChangePasswordParam;
import com.huoyun.business.employee.Employee;
import com.huoyun.business.employee.EmployeeErrorCodes;
import com.huoyun.business.employee.EmployeeService;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;
import com.huoyun.thirdparty.idp.IdpClient;

public class EmployeeServiceImpl implements EmployeeService {

	private BusinessObjectFacade boFacade;

	public EmployeeServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public Employee getEmployeeByUserId(Long userId) {
		String sql = "select t from Employee t where t.userId = :userId";
		TypedQuery<Employee> query = this.boFacade.getBoRepository(Employee.class).newQuery(sql);
		query.setParameter("userId", userId);
		query.setMaxResults(1);

		List<Employee> results = query.getResultList();
		if (results.size() != 0) {
			return results.get(0);
		}

		return null;
	}

	@Override
	public Employee getEmployeeById(Long id) {
		return this.boFacade.getBoRepository(Employee.class).load(id);
	}

	@Override
	public void changePasswordParam(ChangePasswordParam changePasswordParam) throws BusinessException {
		if (StringUtils.isEmpty(changePasswordParam.getOldPassword())) {
			throw new BusinessException(EmployeeErrorCodes.Change_Password_Old_Password_Is_Empty);
		}

		if (StringUtils.isEmpty(changePasswordParam.getNewPassword())) {
			throw new BusinessException(EmployeeErrorCodes.Change_Password_New_Password_Is_Empty);
		}

		if (StringUtils.isEmpty(changePasswordParam.getRepeatNewPassword())) {
			throw new BusinessException(EmployeeErrorCodes.Change_Password_Repeat_New_Password_Is_Empty);
		}

		if (!StringUtils.equals(changePasswordParam.getNewPassword(), changePasswordParam.getRepeatNewPassword())) {
			throw new BusinessException(EmployeeErrorCodes.Change_Password_Password_Not_Match);
		}
		Employee employee = this.boFacade.getCurrentEmployee();

		this.boFacade.getBean(IdpClient.class).changePassword(employee.getUserId(),
				changePasswordParam.getOldPassword(), changePasswordParam.getNewPassword());
	}
}
