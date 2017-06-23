package com.huoyun.business.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huoyun.business.company.Company;
import com.huoyun.business.employee.Employee;

public class InitData {

	private Employee employee;
	private Company company;
	private List<PermissionGroupData> permissionGroups = new ArrayList<>();
	private List<Map<String, Object>> customerStatusList = new ArrayList<>();
	private List<Map<String, Object>> constructionStatusList = new ArrayList<>();

	public List<PermissionGroupData> getPermissionGroups() {
		return permissionGroups;
	}

	public void setPermissionGroups(List<PermissionGroupData> permissionGroups) {
		this.permissionGroups = permissionGroups;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Map<String, Object>> getCustomerStatusList() {
		return customerStatusList;
	}

	public void setCustomerStatusList(List<Map<String, Object>> customerStatusList) {
		this.customerStatusList = customerStatusList;
	}

	public List<Map<String, Object>> getConstructionStatusList() {
		return constructionStatusList;
	}

	public void setConstructionStatusList(List<Map<String, Object>> constructionStatusList) {
		this.constructionStatusList = constructionStatusList;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
