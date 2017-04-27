package com.huoyun.saml2.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huoyun.cookie.CookieUtils;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.saml2.SPSAML2Authentication;
import com.huoyun.saml2.configuration.SAML2Configuration;
import com.huoyun.saml2.configuration.SAML2SPConfigurationCustom;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.huoyun.view.ViewConstants;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.cfg.interfaces.SAML2SPConfiguration;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.common.SAML2Constants;
import com.sap.security.saml2.lib.common.SAML2ErrorResponseDetails;
import com.sap.security.saml2.lib.common.SAML2Exception;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutRequest;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutResponse;
import com.sap.security.saml2.sp.sso.SAML2Authentication;
import com.sap.security.saml2.sp.sso.SLOInfo;
import com.sap.security.saml2.sp.sso.SLORequestInfo;
import com.sap.security.saml2.sp.sso.SLOResponseInfo;

@Controller
@RequestMapping(value = "/saml2")
public class LogoutController {

	private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

	@Autowired
	private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

	@RequestMapping(value = "/sp/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse res) {
		logger.info("Start to logout...");
		CookieUtils.removeCookie(req, res, CookieUtils.DEFAULT_SBO_PATH, CookieUtils.COOKIE_NAME);
		SAML2Configuration saml2Configuration = saml2SPConfigurationFactory.getSAML2Configuration();
		SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();
		HttpSession session = req.getSession(false);

		if (session == null) {
			logger.info("Logout Session not found, redirect to {}", saml2Configuration.getHomePage());
			return "redirect:" + saml2Configuration.getHomePage();
		}

		if (session.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR) != null) {
			session.removeAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR);
		}

		SAML2Principal sessionPrincipal = (SAML2Principal) session
				.getAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR);
		if (sessionPrincipal != null) {
			logger.info("User {} request to log out.", sessionPrincipal.getName());
			try {
				SAML2LogoutRequest logoutRequest = SPSAML2Authentication.getInstance().createSLORequest(spConfiguration,
						sessionPrincipal);
				req.setAttribute(HTTPPostBinding.SAML_REQUEST,
						SAML2Utils.encodeBase64AsString(logoutRequest.generate()));
				req.setAttribute(HTTPPostBinding.SAML_RELAY_STATE, saml2Configuration.getHomePage());
				req.setAttribute("destination", logoutRequest.getDestination());

				logger.info("Logout Invalidate http session.");
				session.invalidate();
				return ViewConstants.View_Logout_Processing;
			} catch (Exception ex) {
				logger.error("Logout occur error", ex);
				return ViewConstants.View_Logout_Processing_Error;
			}
		}

		String sloEndpoint = saml2Configuration.getSloEndpoint();
		logger.info("Logout SAML2Principal in session not found, redirect to {}", sloEndpoint);

		CookieUtils.removeCookie(req, res, CookieUtils.DEFAULT_SBO_PATH, CookieUtils.JSESSION_COOKIE_NAME);
		CookieUtils.removeCookie(req, res, CookieUtils.DEFAULT_SLD_PATH, CookieUtils.JSESSION_COOKIE_NAME);
		CookieUtils.removeCookie(req, res, CookieUtils.DEFAULT_SBO_PATH, CookieUtils.RSESSION_COOKIE_NAME);
		CookieUtils.removeCookie(req, res, CookieUtils.DEFAULT_SLD_PATH, CookieUtils.RSESSION_COOKIE_NAME);
		return "redirect:" + sloEndpoint;
	}

	@RequestMapping(value = "/sp/slo", method = RequestMethod.GET)
	public String getSlo(HttpServletRequest req, HttpServletResponse res) {
		return this.logout(req, res);
	}

	@RequestMapping(value = "/sp/slo", method = RequestMethod.POST)
	public String postSlo(@RequestParam(HTTPPostBinding.SAML_RESPONSE) String samlResponse,
			@RequestParam(name = HTTPPostBinding.SAML_RELAY_STATE, required = false) String relayState,
			HttpServletRequest req, HttpServletResponse res) {
		logger.info("Enter slo controller ...");
		logger.info("SAML Response: {}", samlResponse);
		logger.info("SAML Relay State: {}", relayState);

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(HTTPPostBinding.SAML_RESPONSE, samlResponse);
		parameters.put(HTTPPostBinding.SAML_RELAY_STATE, relayState);

		SAML2Configuration saml2Configuration = saml2SPConfigurationFactory.getSAML2Configuration();
		SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();

		try {
			SLOInfo sloInfo = SAML2Authentication.getInstance().validateSLOMessageHttpBody(spConfiguration, parameters);
			if (sloInfo instanceof SLORequestInfo) {
				return handleSLORequest(req, res, saml2Configuration, spConfiguration, (SLORequestInfo) sloInfo);
			} else if (sloInfo instanceof SLOResponseInfo) {
				String sloStatus = getSLOStatusDetails((SLOResponseInfo) sloInfo);
				logger.info("Logout status: {}", sloStatus);
				String home = saml2Configuration.getHomePage();
				return "redirect:" + home;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String handleSLORequest(HttpServletRequest req, HttpServletResponse resp,
			SAML2Configuration saml2Configuration, SAML2SPConfigurationCustom spConfiguration,
			SLORequestInfo sloRequestInfo)
			throws IOException, SAML2Exception, SAML2ConfigurationException, ServletException {
		List<String> receivedSessionIndexes = sloRequestInfo.getSessionIndexes();

		HttpSession httpSession = req.getSession(false);
		if (httpSession == null) {
			SAML2ErrorResponseDetails details = new SAML2ErrorResponseDetails(sloRequestInfo.getId(),
					sloRequestInfo.getIssuer(), SAML2Constants.STATUS_CODE_TOP_LEVEL_RESPONDER,
					SAML2Constants.STATUS_CODE_SECOND_LEVEL_REQUEST_DENIED, "Session invalid already");
			return sendErrorResponse(req, spConfiguration, details);
		}

		SAML2Principal principal = (SAML2Principal) httpSession
				.getAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR);
		if (principal == null) {
			SAML2ErrorResponseDetails details = new SAML2ErrorResponseDetails(sloRequestInfo.getId(),
					sloRequestInfo.getIssuer(), SAML2Constants.STATUS_CODE_TOP_LEVEL_RESPONDER,
					SAML2Constants.STATUS_CODE_SECOND_LEVEL_REQUEST_DENIED, "Principle doesn't exist");
			return sendErrorResponse(req, spConfiguration, details);
		}

		List<String> userSessionIndexes = principal.getSessionIndexes();
		for (String sessionIndex : receivedSessionIndexes) {
			if (userSessionIndexes.contains(sessionIndex)) {
				httpSession.invalidate();
				SAML2LogoutResponse logoutResponse = SPSAML2Authentication.getInstance()
						.createSLOResponse(spConfiguration, sloRequestInfo);
				req.setAttribute(HTTPPostBinding.SAML_RESPONSE,
						SAML2Utils.encodeBase64AsString(logoutResponse.generate()));
				req.setAttribute("destination", logoutResponse.getDestination());
				return ViewConstants.Page_Login_Processing;
			}
		}

		SAML2ErrorResponseDetails details = new SAML2ErrorResponseDetails(sloRequestInfo.getId(),
				sloRequestInfo.getIssuer(), SAML2Constants.STATUS_CODE_TOP_LEVEL_RESPONDER,
				SAML2Constants.STATUS_CODE_SECOND_LEVEL_REQUEST_DENIED, "Invalid SessionIndexes received");
		return sendErrorResponse(req, spConfiguration, details);
	}

	private String sendErrorResponse(HttpServletRequest req, SAML2SPConfiguration spConfig,
			SAML2ErrorResponseDetails details)
			throws IOException, SAML2Exception, SAML2ConfigurationException, ServletException {
		SAML2LogoutResponse logoutResponse = SPSAML2Authentication.getInstance().createSLOErrorResponse(spConfig,
				details);
		req.setAttribute(HTTPPostBinding.SAML_RESPONSE, SAML2Utils.encodeBase64AsString(logoutResponse.generate()));
		req.setAttribute("destination", logoutResponse.getDestination());
		return ViewConstants.View_Logout_Processing;
	}

	private String getSLOStatusDetails(SLOResponseInfo sloResponseInfo) {
		StringBuilder builder = new StringBuilder();
		if (SAML2Constants.STATUS_CODE_TOP_LEVEL_SUCCESS.equals(sloResponseInfo.getStatusCode())) {
			builder.append("Success");
		} else {
			builder.append("Error status code: ").append(sloResponseInfo.getStatusCode());
			builder.append("; Second level status code: ").append(sloResponseInfo.getSecondaryStatusCode());
			builder.append("; Status message: ").append(sloResponseInfo.getStatusMessage());
		}
		return builder.toString();
	}
}
