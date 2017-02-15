package com.huoyun.core.bo.metadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.core.bo.metadata.impl.MetadataRepositoryImpl;
import com.huoyun.locale.LocaleService;

@Configuration
public class MetadataAutoConfiguration {

	@Bean
	public MetadataRepository metadataRepository(LocaleService localeService){
		return new MetadataRepositoryImpl(localeService);
	}
}
