package com.huoyun.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BoPropertyRule;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.validator.RuleType;

@BoEntity
@Entity
@Table
public class Contact extends AbstractBusinessObjectImpl {

	public Contact() {
	}

	public Contact(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	//@SequenceGenerator(name = "Contact_SEQ", sequenceName = "CONTACT_SEQ")
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Contact_SEQ")
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	@BoProperty
	private String firstName;

	@BoProperty
	@BoPropertyRule(rule = RuleType.StartsWith, expr = "Jing")
	private String lastName;

	@BoProperty(type = PropertyType.Email, mandatory = true)
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
