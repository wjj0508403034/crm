package com.huoyun.core.multitenant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huoyun.saml2.EndpointsConstatns;

public class TenantInterceptor extends HandlerInterceptorAdapter {

	private static Logger LOGGER = LoggerFactory
			.getLogger(TenantInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOGGER.info("Request {} begin tenant interceptor ...",
				request.getRequestURI());
		HttpSession session = request.getSession();
		if (session != null) {
			String tenantCode = (String) session
					.getAttribute(EndpointsConstatns.SSO_TENANT_CODE);
			if (!StringUtils.isEmpty(tenantCode)) {
				TenantContext.setCurrentTenantCode(tenantCode);
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.destory();
		LOGGER.info("Request {} end tenant interceptor.",
				request.getRequestURI());
	}
}
