package com.huoyun.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.saml2.SPSAML2Authentication;
import com.huoyun.saml2.configuration.SAML2Configuration;
import com.huoyun.saml2.configuration.SAML2SPConfigurationCustom;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactoryAware;
import com.huoyun.view.ViewConstants;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2AuthRequest;

public class LoginRequiredFilter implements Filter, SAML2SPConfigurationFactoryAware {
	private static final Logger logger = LoggerFactory.getLogger(LoginRequiredFilter.class);

	private FilterConfig filterConfig;

	private final List<String> excludes = new ArrayList<String>();

	private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

	@Override
	public void setSAML2SPConfigurationFactory(SAML2SPConfigurationFactory saml2spConfigurationFactory) {
		this.saml2SPConfigurationFactory = saml2spConfigurationFactory;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		String strExcludes = this.filterConfig.getInitParameter("EXCLUDES");
		if (null != strExcludes && !strExcludes.trim().equals(""))
			excludes.addAll(Arrays.asList(strExcludes.split(";")));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			SAML2Configuration saml2Configuration = saml2SPConfigurationFactory.getSAML2Configuration();

			boolean isSkip = false;
			String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
			for (String s : excludes) {
				if (path.startsWith(s)) {
					isSkip = true;
					break;
				}
			}

			if (isSkip) {
				logger.info("Request {} is skipping SSO authorization.", httpRequest.getRequestURI());
				chain.doFilter(request, response);
				return;
			}

			HttpServletResponse httpResponse = (HttpServletResponse) response;

			HttpSession session = httpRequest.getSession(false);
			if (null != session && null != session.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR)) {
				chain.doFilter(request, response);
			} else {

				if (httpRequest.getHeader("Accept") != null && httpRequest.getHeader("Accept").contains("json")) {
					httpResponse.setStatus(401);
					return;
				}

				logger.info("Request is not authorized, require for #SSO authorization. Request URL : {}",
						request instanceof HttpServletRequest ? ((HttpServletRequest) request).getRequestURI() : null);

				SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();
				String acsUrl = this.saml2SPConfigurationFactory.getAcsUrl(httpRequest);
				String relayState = getRealyState(httpRequest);

				logger.info("Sending SSO HTTPPostBinding, acsUrl {}, relayState {}.", acsUrl, relayState);

				SAML2AuthRequest authnRequest = SPSAML2Authentication.getInstance()
						.createSAML2AuthRequest(spConfiguration, saml2Configuration.getDefaultIdPName(), acsUrl);
				httpRequest.setAttribute(HTTPPostBinding.SAML_REQUEST,
						SAML2Utils.encodeBase64AsString(authnRequest.generate()));
				httpRequest.setAttribute(HTTPPostBinding.SAML_RELAY_STATE, relayState);
				httpRequest.setAttribute("destination", authnRequest.getDestination());
				httpRequest.getServletContext().getRequestDispatcher(ViewConstants.Page_Login_Processing)
						.forward(httpRequest, httpResponse);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
	}

	private String getRealyState(HttpServletRequest httpRequest) {
		StringBuilder sb = new StringBuilder();
		sb.append(httpRequest.getScheme()).append("://").append(httpRequest.getServerName());
		if ((httpRequest.getScheme().equals("https") && httpRequest.getServerPort() != 443)
				|| (httpRequest.getScheme().equals("http") && httpRequest.getServerPort() != 80)) {
			sb.append(":").append(httpRequest.getServerPort());
		}
		sb.append(httpRequest.getContextPath()).append(httpRequest.getServletPath());

		if (httpRequest.getMethod().equalsIgnoreCase("POST")) {
			return sb.toString();
		}
		if (httpRequest.getQueryString() != null)
			sb.append("?").append(httpRequest.getQueryString());
		return sb.toString();
	}

}
