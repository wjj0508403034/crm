package com.huoyun.business.finishwork;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;
import com.huoyun.upload.Attachment;

@BoEntity(allowCustomized = false)
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class FinishWorkDesignPhoto extends LiteBusinessObject {

	public FinishWorkDesignPhoto() {
	}

	public FinishWorkDesignPhoto(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn
	@BoProperty(type = PropertyType.Image, searchable = false)
	private Attachment photo;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private FinishWork finishWork;

	@Override
	public Long getId() {
		return this.id;
	}

	public Attachment getPhoto() {
		return photo;
	}

	public void setPhoto(Attachment photo) {
		this.photo = photo;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public FinishWork getFinishWork() {
		return finishWork;
	}

	public void setFinishWork(FinishWork finishWork) {
		this.finishWork = finishWork;
	}
}
