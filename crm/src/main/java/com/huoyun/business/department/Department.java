package com.huoyun.business.department;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.annotation.ValidValue;
import com.huoyun.core.bo.annotation.ValidValues;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity(allowCustomized = false)
@Entity
@Table(indexes = { @Index(name = "UNIQUDEPARTMENTCODE", columnList = "departmentCode,tenantCode", unique = true) })
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class Department extends LiteBusinessObject {

	public Department() {

	}

	public Department(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BoProperty(mandatory = true)
	private String departmentCode;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String departmentName;

	@BoProperty(type = PropertyType.Text, searchable = false)
	private String description;

	@ValidValues(validValues = { @ValidValue(value = "business"),
			@ValidValue(value = "design"), @ValidValue(value = "finance"),
			@ValidValue(value = "management"),
			@ValidValue(value = "supervision"),
			@ValidValue(value = "construction"),
			@ValidValue(value = "CAD_drawing"),
			@ValidValue(value = "effect_drawing"),
			@ValidValue(value = "material") })
	@BoProperty
	private String category;

	@ValidValues(validValues = { @ValidValue(value = "enable"),
			@ValidValue(value = "disable") })
	@BoProperty
	private String status;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
