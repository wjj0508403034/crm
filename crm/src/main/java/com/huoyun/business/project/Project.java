package com.huoyun.business.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.business.customer.Customer;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;

@BoEntity
@Entity
@Table
public class Project extends AbstractBusinessObjectImpl {

	public Project() {
	}

	public Project(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BoProperty
	private String status;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Customer customer;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
