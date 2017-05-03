package com.huoyun.business.finishwork;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.upload.Attachment;

@BoEntity(allowCustomized = false)
@Entity
@Table
public class FinishWorkDesignPhoto extends LiteBusinessObject {

	public FinishWorkDesignPhoto() {
	}

	public FinishWorkDesignPhoto(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
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
