package com.huoyun.business.company;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.upload.Attachment;

@BoEntity
@Entity
@Table
public class Company extends LiteBusinessObject {

	public Company() {

	}

	public Company(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String companyName;

	@BoProperty
	private String description;

	@ManyToOne
	@JoinColumn
	@BoProperty(type = PropertyType.Image, searchable = false)
	private Attachment logo;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Attachment getLogo() {
		return logo;
	}

	public void setLogo(Attachment logo) {
		this.logo = logo;
	}

}
