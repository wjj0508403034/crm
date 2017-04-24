package com.huoyun.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.business.department.Department;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.annotation.ValidValue;
import com.huoyun.core.bo.annotation.ValidValues;

@BoEntity
@Entity
@Table
public class Employee extends AbstractBusinessObjectImpl {

	public Employee() {
	}

	public Employee(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	@BoProperty(readonly = true)
	private Long userId;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String userName;

	@BoProperty(mandatory = true)
	private String phone;

	@BoProperty(mandatory = true)
	private String email;

	@ValidValues(validValues = { @ValidValue(value = "general"), @ValidValue(value = "business_director"),
			@ValidValue(value = "design_director"), @ValidValue(value = "department_manager") })
	@BoProperty
	private String title;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Department department;

	@ValidValues(validValues = { @ValidValue(value = "enable"), @ValidValue(value = "disable") })
	@BoProperty
	private String status;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
