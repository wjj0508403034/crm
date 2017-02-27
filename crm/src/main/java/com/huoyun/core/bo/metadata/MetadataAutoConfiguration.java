package com.huoyun.core.bo.metadata;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.core.bo.metadata.events.MetadataChangedEventListener;
import com.huoyun.core.bo.metadata.events.MetadataChangedPublisher;
import com.huoyun.core.bo.metadata.impl.MetadataRepositoryImpl;
import com.huoyun.core.classloader.CachedClassLoader;
import com.huoyun.locale.LocaleService;
import com.huoyun.upgrade.UpgradeAutoConfiguration;

@AutoConfigureAfter(UpgradeAutoConfiguration.class)
@Configuration
public class MetadataAutoConfiguration {

	@Bean(name = MetadataRepositoryImpl.Name)
	public MetadataRepository metadataRepository(LocaleService localeService,
			CachedClassLoader classLoader) {
		return new MetadataRepositoryImpl(localeService, classLoader);
	}

	@Bean
	public MetadataChangedPublisher metadataChangedPublisher(
			ApplicationEventPublisher publisher) {
		return new MetadataChangedPublisher(publisher);
	}

	@Bean
	public MetadataChangedEventListener metadataChangedEventListener() {
		return new MetadataChangedEventListener();
	}
}
