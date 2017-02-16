package com.huoyun.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.DefaultBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;

@BoEntity
@Entity
@Table
public class Contact extends DefaultBusinessObject {

	public Contact() {
	}

	public Contact(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = "common.bo.id")
	private Long id;

	@BoProperty
	private String firstName;

	@BoProperty
	private String lastName;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


}
