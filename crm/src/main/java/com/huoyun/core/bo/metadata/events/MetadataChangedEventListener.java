package com.huoyun.core.bo.metadata.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import com.huoyun.core.bo.metadata.MetadataRepository;

public class MetadataChangedEventListener implements
		ApplicationListener<MetadataChangedEvent>, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MetadataChangedEventListener.class);

	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(MetadataChangedEvent event) {
		LOGGER.info("Received the metadata changed event, start refresh the metadata ...");
		if (this.applicationContext != null) {
			MetadataRepository metaRepo = this.applicationContext
					.getBean(MetadataRepository.class);
			if (metaRepo != null) {
				metaRepo.refresh();
			}
		}
		
		LOGGER.info("Refresh the metadata successfully.");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
