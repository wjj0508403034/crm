package com.huoyun.web;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.HttpSessionMutexListener;

import com.huoyun.login.LoginRequiredFilter;
import com.huoyun.saml2.filters.ACSServlet;
import com.huoyun.saml2.filters.LogoutTriggerServlet;
import com.huoyun.saml2.filters.SLOServlet;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ServletListenerRegistrationBean<EventListener> getDemoListener() {
		ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<>();
		registrationBean.setListener(new HttpSessionMutexListener());
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean loginRequiredFilter() {
		LoginRequiredFilter loginRequiredFilter = new LoginRequiredFilter();
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(loginRequiredFilter);
		Map<String,String> params = new HashMap<>();
		params.put("LOCALE_SESSION_ATTRIBUTE_NAME", "sbo.locale");
		params.put("EXCLUDES","/oauth2/token;/libs;/image;/resources/js;/fonts;/js;cache.manifest;/sso;/templates;/resources;/css;/js");
		registrationBean.setInitParameters(params);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");// 拦截路径，可以添加多个
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setName("LoginRequiredFilter");
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean acsServlet() {
		ACSServlet acsServlet = new ACSServlet();
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(acsServlet);
		List<String> urlMappings = new ArrayList<String>();
		urlMappings.add("/saml2/sp/acs");// //访问，可以添加多个
		registrationBean.setUrlMappings(urlMappings);
		registrationBean.setLoadOnStartup(1);
		registrationBean.setName("ACSServlet");
		return registrationBean;
	}

	@Bean
	public ServletRegistrationBean logoutTriggerServlet() {
		LogoutTriggerServlet logoutTriggerServlet = new LogoutTriggerServlet();
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
	public ServletRegistrationBean sloServlet() {
		SLOServlet sloServlet = new SLOServlet();
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
		//registry.addInterceptor(new LoginRequiredInterceptor());
	}
}
