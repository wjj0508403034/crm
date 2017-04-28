package com.huoyun.business.employee.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.employee.EmployeeService;
import com.huoyun.core.bo.BusinessObjectFacade;

public class EmployeeServiceImpl implements EmployeeService {

	private BusinessObjectFacade boFacade;

	public EmployeeServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Override
	public Employee getEmployeeByUserId(Long userId) {
		String sql = "select t from Employee t where t.userId = :userId";
		TypedQuery<Employee> query = this.boFacade.getBoRepository(
				Employee.class).newQuery(sql);
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
}
