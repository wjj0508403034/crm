package com.huoyun.business.house;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity(allowCustomized = false)
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class HouseType extends LiteBusinessObject {

	public HouseType() {
	}

	public HouseType(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String name;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
