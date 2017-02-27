package com.huoyun.core.bo.metadata.events;

import org.springframework.context.ApplicationEvent;

public class MetadataChangedEvent extends ApplicationEvent {

	private static final long serialVersionUID = -7981674625645287916L;

	private Action action;
	
	public MetadataChangedEvent(Object source) {
		super(source);

	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
