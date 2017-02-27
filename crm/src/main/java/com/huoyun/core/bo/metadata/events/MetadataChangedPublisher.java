package com.huoyun.core.bo.metadata.events;

import org.springframework.context.ApplicationEventPublisher;

public class MetadataChangedPublisher {

	private ApplicationEventPublisher publisher;

	public MetadataChangedPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	public void publish() {
		MetadataChangedEvent ce = new MetadataChangedEvent(this);
		this.publisher.publishEvent(ce);
	}

}
