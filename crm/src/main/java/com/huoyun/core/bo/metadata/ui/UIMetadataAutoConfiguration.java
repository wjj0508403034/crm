package com.huoyun.core.bo.metadata.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.ui.impl.UIMetadataRepositoryImpl;
import com.huoyun.exception.BusinessException;

@Configuration
public class UIMetadataAutoConfiguration {

	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Bean
	public UIMetadataRepository uiMetadataRepository(
			BusinessObjectFacade boFacade, UIMetaLoader uiMetaLoader) {
		return new UIMetadataRepositoryImpl(boFacade, uiMetaLoader);
	}

	@Bean
	public UIMetaLoader uiMetaLoader() throws BusinessException {
		return UIMetaLoader.getInstance();
	}
}
