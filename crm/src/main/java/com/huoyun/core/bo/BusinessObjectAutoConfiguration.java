package com.huoyun.core.bo;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.core.bo.ext.impl.ExtensionServiceImpl;
import com.huoyun.core.bo.impl.BusinessObjectFacadeImpl;
import com.huoyun.core.bo.impl.BusinessObjectMapperImpl;
import com.huoyun.core.bo.impl.BusinessObjectServiceImpl;
import com.huoyun.core.bo.metadata.MetadataAutoConfiguration;
import com.huoyun.core.jpa.JpaAutoConfiguration;

@AutoConfigureAfter({ MetadataAutoConfiguration.class,
		JpaAutoConfiguration.class })
@Configuration
public class BusinessObjectAutoConfiguration {

	@Bean
	public BusinessObjectFacade boFacade(ApplicationContext context) {
		return new BusinessObjectFacadeImpl(context);
	}

	@Bean
	public BusinessObjectService businessObjectService(
			BusinessObjectFacade boFacade,BusinessObjectMapper boMapper) {
		return new BusinessObjectServiceImpl(boFacade,boMapper);
	}

	@Bean
	public ExtensionService extensionService(BusinessObjectFacade boFacade) {
		return new ExtensionServiceImpl(boFacade);
	}

	@Bean
	public BusinessObjectMapper businessObjectMapper() {
		return new BusinessObjectMapperImpl();
	}
}
