package com.huoyun.web;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.HttpSessionMutexListener;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.multitenant.TenantInterceptor;
import com.huoyun.login.LoginProcessor;
import com.huoyun.login.LoginRequiredFilter;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public LoginProcessor loginProcessor(BusinessObjectFacade boFacade){
		return new LoginProcessor(boFacade);
	}
	
	@Bean
	public ServletListenerRegistrationBean<EventListener> getDemoListener() {
		ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<>();
		registrationBean.setListener(new HttpSessionMutexListener());
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean loginRequiredFilter(
			SAML2SPConfigurationFactory saml2SPConfigurationFactory) {
		LoginRequiredFilter loginRequiredFilter = new LoginRequiredFilter();
		loginRequiredFilter
				.setSAML2SPConfigurationFactory(saml2SPConfigurationFactory);
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(loginRequiredFilter);
		Map<String, String> params = new HashMap<>();
		params.put(
				"EXCLUDES",
				"/oauth2/token;/libs;/image;/resources/js;/fonts;/js;cache.manifest;/sso/waiting.html;/templates;/resources;/css;/js;/saml2/sp/acs;");
		registrationBean.setInitParameters(params);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setName("LoginRequiredFilter");
		registrationBean.setOrder(1);
		return registrationBean;
	}



	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TenantInterceptor());
	}
}
