package com.huoyun.core.bo.ext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity(allowCustomized = false)
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class UserPropertyValidValue extends LiteBusinessObject {

	public UserPropertyValidValue() {

	}

	public UserPropertyValidValue(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	private String value;

	private String label;

	private boolean disable;

	@ManyToOne
	@JoinColumn(nullable = false)
	private UserProperty userProperty;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProperty getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(UserProperty userProperty) {
		this.userProperty = userProperty;
	}

}
