package com.huoyun.core.bo;

import javax.persistence.MappedSuperclass;

import com.huoyun.core.bo.annotation.BoProperty;

@MappedSuperclass
public abstract class DefaultBusinessObject extends AbstractBusinessObject {

	public DefaultBusinessObject() {
	}

	public DefaultBusinessObject(BusinessObjectFacade boFacade) {
		if (null == this.boFacade) {
			this.boFacade = boFacade;
		}
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
}
