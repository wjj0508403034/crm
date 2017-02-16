package com.huoyun.core.bo;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.joda.time.DateTime;

import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.impl.BoRepositoryImpl;

@MappedSuperclass
public abstract class DefaultBusinessObject extends AbstractBusinessObject {

	@Transient
	protected BoRepositoryImpl<DefaultBusinessObject> boRepository;

	public DefaultBusinessObject() {
	}

	public DefaultBusinessObject(BusinessObjectFacade boFacade) {
		this.setBoFacade(boFacade);
	}

	@BoProperty(readonly = true, label = "common.bo.ownerCode")
	private Long ownerCode;

	@BoProperty(readonly = true, label = "common.bo.creatorCode")
	protected Long creatorCode;

	@BoProperty(readonly = true, label = "common.bo.updateCode")
	protected Long updateCode;

	public Long getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(Long ownerCode) {
		this.ownerCode = ownerCode;
	}

	public Long getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(Long creatorCode) {
		this.creatorCode = creatorCode;
	}

	public Long getUpdateCode() {
		return updateCode;
	}

	public void setUpdateCode(Long updateCode) {
		this.updateCode = updateCode;
	}

	@PostLoad
	public void postLoadBO() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setBoFacade(BusinessObjectFacade boFacade) {
		if (null == this.boFacade) {
			this.boFacade = boFacade;
			this.boRepository = (BoRepositoryImpl<DefaultBusinessObject>) this.boFacade
					.getBoRepository(this.getClass());
		}
	}

	@Override
	public final void create() {
		super.create();
		this.boRepository.save(this);
		this.boRepository.flush();
	}
	
	@Override
	public final void update() {
		super.update();
		this.boRepository.update(this);
		this.boRepository.flush();
	}

	@Override
	public void delete() {
		this.boRepository.delete(this);
	}
}
