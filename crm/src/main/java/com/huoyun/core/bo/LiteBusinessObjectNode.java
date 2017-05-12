package com.huoyun.core.bo;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class LiteBusinessObjectNode extends LiteBusinessObject implements BusinessObjectNode {

	public LiteBusinessObjectNode() {
	}

	public LiteBusinessObjectNode(BusinessObjectFacade boFacade) {
		this.setBoFacade(boFacade);
	}
}
