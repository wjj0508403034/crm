package com.huoyun.session.impl;

import java.io.Serializable;

import com.huoyun.business.employee.Employee;
import com.huoyun.session.Session;

public class SessionImpl implements Session, Serializable {

	private static final long serialVersionUID = 3645996160627079122L;

	private Employee employee;

	public SessionImpl(Employee employee) {
		this.employee = employee;
	}

	@Override
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
