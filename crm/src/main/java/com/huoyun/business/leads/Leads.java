package com.huoyun.business.leads;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.business.customer.SalesSource;
import com.huoyun.business.house.Houses;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.PropertyType;

@BoEntity
@Entity
@Table
public class Leads extends AbstractBusinessObjectImpl {

	public Leads() {
	}

	public Leads(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BoProperty
	private String name;

	@BoProperty
	private String telephone;

	@BoProperty(type = PropertyType.Text, searchable = false)
	private String description;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private LeadsStatus status;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private SalesSource salesSource;

	@ManyToOne
	@JoinColumn
	@BoProperty(searchable = false)
	private Houses houses;

	@BoProperty
	private boolean transform;

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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LeadsStatus getStatus() {
		return status;
	}

	public void setStatus(LeadsStatus status) {
		this.status = status;
	}

	public SalesSource getSalesSource() {
		return salesSource;
	}

	public void setSalesSource(SalesSource salesSource) {
		this.salesSource = salesSource;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isTransform() {
		return transform;
	}

	public void setTransform(boolean transform) {
		this.transform = transform;
	}

	public Houses getHouses() {
		return houses;
	}

	public void setHouses(Houses houses) {
		this.houses = houses;
	}
}
