package com.huoyun.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;

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
	// @SequenceGenerator(name = "Contact_SEQ", sequenceName = "CONTACT_SEQ")
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "Contact_SEQ")
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;
	
	@BoProperty
	private Long userId;
	

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

}
