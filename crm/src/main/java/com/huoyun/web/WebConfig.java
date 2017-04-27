package com.huoyun.web;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.HttpSessionMutexListener;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.login.LoginProcessor;
import com.huoyun.login.LoginRequiredFilter;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.huoyun.saml2.filters.ACSServlet;
import com.huoyun.saml2.filters.LogoutTriggerServlet;
import com.huoyun.saml2.filters.SLOServlet;

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
		params.put("LOCALE_SESSION_ATTRIBUTE_NAME", "sbo.locale");
		params.put(
				"EXCLUDES",
				"/oauth2/token;/libs;/image;/resources/js;/fonts;/js;cache.manifest;/sso;/templates;/resources;/css;/js");
		registrationBean.setInitParameters(params);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setName("LoginRequiredFilter");
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean acsServlet(
			SAML2SPConfigurationFactory saml2SPConfigurationFactory,
			ApplicationContext context) {
		
		ACSServlet acsServlet = new ACSServlet(context);
		acsServlet.setSAML2SPConfigurationFactory(saml2SPConfigurationFactory);
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(acsServlet);
		List<String> urlMappings = new ArrayList<String>();
		urlMappings.add("/saml2/sp/acs");
		registrationBean.setUrlMappings(urlMappings);
		registrationBean.setLoadOnStartup(1);
		registrationBean.setName("ACSServlet");
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean logoutTriggerServlet(
			SAML2SPConfigurationFactory saml2SPConfigurationFactory) {
		LogoutTriggerServlet logoutTriggerServlet = new LogoutTriggerServlet();
		logoutTriggerServlet
				.setSAML2SPConfigurationFactory(saml2SPConfigurationFactory);
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(logoutTriggerServlet);
		List<String> urlMappings = new ArrayList<String>();
		urlMappings.add("/saml2/sp/logout");
		registrationBean.setUrlMappings(urlMappings);
		registrationBean.setLoadOnStartup(1);
		registrationBean.setName("LogoutTriggerServlet");
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean sloServlet(
			SAML2SPConfigurationFactory saml2SPConfigurationFactory) {
		SLOServlet sloServlet = new SLOServlet();
		sloServlet.setSAML2SPConfigurationFactory(saml2SPConfigurationFactory);
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(sloServlet);
		List<String> urlMappings = new ArrayList<String>();
		urlMappings.add("/saml2/sp/slo");
		registrationBean.setUrlMappings(urlMappings);
		registrationBean.setLoadOnStartup(1);
		registrationBean.setName("SLOServlet");
		return registrationBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(new LoginRequiredInterceptor());
	}
}
