package com.huoyun.business.customer.trace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.business.customer.Customer;
import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;

@BoEntity(allowCustomized = false)
@Entity
@Table
public class CustomerTraceRecord extends LiteBusinessObject {

	public CustomerTraceRecord() {
	}

	public CustomerTraceRecord(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BoProperty
	private String comment;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Customer customer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee owner;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public Employee getOwner() {
		return owner;
	}

	public void setOwner(Employee owner) {
		this.owner = owner;
	}
}
