package com.huoyun.business.employee;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huoyun.business.department.Department;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.annotation.ValidValue;
import com.huoyun.core.bo.annotation.ValidValues;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.upload.Attachment;

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
	@BoProperty(label = I18n_Label_Id, searchable = false)
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn
	@BoProperty
	private Department department;

	@ValidValues(validValues = { @ValidValue(value = "enable"), @ValidValue(value = "disable") })
	@BoProperty
	private String status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn
	@BoProperty(type = PropertyType.Image, searchable = false)
	private Attachment avatar;

	@Transient
	private String tenantCode;

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

	public Attachment getAvatar() {
		return avatar;
	}

	public void setAvatar(Attachment avatar) {
		this.avatar = avatar;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}


}
