package com.huoyun.business.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.ValidValues;
import com.huoyun.core.bo.annotation.ValidValue;
import com.huoyun.employee.Employee;

@BoEntity
@Entity
@Table
public class Customer extends AbstractBusinessObjectImpl {

	public Customer() {
	}

	public Customer(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	@BoProperty
	private String name;

	@BoProperty
	private String contact;

	@BoProperty
	private String community;

	@ManyToOne
	@JoinColumn(name = "SALESMAN_ID")
	@BoProperty
	private Employee salesman;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee designer;

	@BoProperty
	private String status;

	@ValidValues(validValues = {
			@ValidValue( value = "recommend"),
			@ValidValue( value = "roadShow") 
			})
	@BoProperty
	private String salesSource;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public Employee getSalesman() {
		return salesman;
	}

	public void setSalesman(Employee salesman) {
		this.salesman = salesman;
	}

	public Employee getDesigner() {
		return designer;
	}

	public void setDesigner(Employee designer) {
		this.designer = designer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSalesSource() {
		return salesSource;
	}

	public void setSalesSource(String salesSource) {
		this.salesSource = salesSource;
	}

}
