package com.huoyun.core.bo.metadata;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
