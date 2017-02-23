package com.huoyun.core.bo.ext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.ext.metadata.ExtMetadataRepositoryImpl;
import com.huoyun.core.bo.metadata.MetadataRepository;
import com.huoyun.core.bo.metadata.impl.MetadataRepositoryImpl;

@Configuration
public class ExtensionAutoConfiguration {

	@Primary
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Bean
	public MetadataRepository extMetadataRepository(
			MetadataRepositoryImpl metadataRepository,
			BusinessObjectFacade boFacade) {
		MetadataRepository extMetadataRepository = new ExtMetadataRepositoryImpl(
				metadataRepository, boFacade);
		return extMetadataRepository;
	}
}
