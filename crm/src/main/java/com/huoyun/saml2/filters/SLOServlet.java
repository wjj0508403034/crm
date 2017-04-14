package com.huoyun.saml2.filters;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.saml2.SAML2Authentication;
import com.huoyun.saml2.configuration.SAML2Configuration;
import com.huoyun.saml2.configuration.SAML2SPConfigurationCustom;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactoryAware;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.cfg.interfaces.SAML2SPConfiguration;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.common.SAML2Constants;
import com.sap.security.saml2.lib.common.SAML2ErrorResponseDetails;
import com.sap.security.saml2.lib.common.SAML2Exception;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutResponse;
import com.sap.security.saml2.sp.sso.SLOInfo;
import com.sap.security.saml2.sp.sso.SLORequestInfo;
import com.sap.security.saml2.sp.sso.SLOResponseInfo;

public class SLOServlet extends HttpServlet implements
		SAML2SPConfigurationFactoryAware {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(SLOServlet.class);

	private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

	@Override
	public void setSAML2SPConfigurationFactory(
			SAML2SPConfigurationFactory saml2spConfigurationFactory) {
		this.saml2SPConfigurationFactory = saml2spConfigurationFactory;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			SAML2Configuration saml2Configuration = saml2SPConfigurationFactory
					.getSAML2Configuration();
			SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory
					.createSAML2SPConfiguration();

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(HTTPPostBinding.SAML_REQUEST,
					req.getParameter(HTTPPostBinding.SAML_REQUEST));
			parameters.put(HTTPPostBinding.SAML_RESPONSE,
					req.getParameter(HTTPPostBinding.SAML_RESPONSE));
			parameters.put(HTTPPostBinding.SAML_RELAY_STATE,
					req.getParameter(HTTPPostBinding.SAML_RELAY_STATE));

			SLOInfo sloInfo = com.sap.security.saml2.sp.sso.SAML2Authentication
					.getInstance().validateSLOMessageHttpBody(spConfiguration,
							parameters);
			if (sloInfo instanceof SLORequestInfo) {
				handleSLORequest(req, resp, saml2Configuration,
						spConfiguration, (SLORequestInfo) sloInfo);
			} else if (sloInfo instanceof SLOResponseInfo) {
				handleSLOResponse(req, resp, saml2Configuration,
						spConfiguration, (SLOResponseInfo) sloInfo);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void handleSLORequest(HttpServletRequest req,
			HttpServletResponse resp, SAML2Configuration saml2Configuration,
			SAML2SPConfigurationCustom spConfiguration,
			SLORequestInfo sloRequestInfo) throws IOException, SAML2Exception,
			SAML2ConfigurationException, ServletException {
		List<String> receivedSessionIndexes = sloRequestInfo
				.getSessionIndexes();

		HttpSession httpSession = req.getSession(false);
		if (httpSession == null) {
			SAML2ErrorResponseDetails details = new SAML2ErrorResponseDetails(
					sloRequestInfo.getId(), sloRequestInfo.getIssuer(),
					SAML2Constants.STATUS_CODE_TOP_LEVEL_RESPONDER,
					SAML2Constants.STATUS_CODE_SECOND_LEVEL_REQUEST_DENIED,
					"Session invalid already");
			sendErrorResponse(req, resp, spConfiguration, details);
			return;
		}

		SAML2Principal principal = (SAML2Principal) httpSession
				.getAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR);
		if (principal == null) {
			SAML2ErrorResponseDetails details = new SAML2ErrorResponseDetails(
					sloRequestInfo.getId(), sloRequestInfo.getIssuer(),
					SAML2Constants.STATUS_CODE_TOP_LEVEL_RESPONDER,
					SAML2Constants.STATUS_CODE_SECOND_LEVEL_REQUEST_DENIED,
					"Principle doesn't exist");
			sendErrorResponse(req, resp, spConfiguration, details);
			return;
		}

		List<String> userSessionIndexes = principal.getSessionIndexes();
		for (String sessionIndex : receivedSessionIndexes) {
			if (userSessionIndexes.contains(sessionIndex)) {
				httpSession.invalidate();
				SAML2LogoutResponse logoutResponse = SAML2Authentication
						.getInstance().createSLOResponse(spConfiguration,
								sloRequestInfo);
				req.setAttribute(HTTPPostBinding.SAML_RESPONSE, SAML2Utils
						.encodeBase64AsString(logoutResponse.generate()));
				req.setAttribute("destination", logoutResponse.getDestination());
				req.getServletContext().getRequestDispatcher("/waiting.jsp")
						.forward(req, resp);
				return;
			}
		}

		// As none of the received SessionIndexes corresponds to user
		// session return error LogoutResponse to the IdP
		SAML2ErrorResponseDetails details = new SAML2ErrorResponseDetails(
				sloRequestInfo.getId(), sloRequestInfo.getIssuer(),
				SAML2Constants.STATUS_CODE_TOP_LEVEL_RESPONDER,
				SAML2Constants.STATUS_CODE_SECOND_LEVEL_REQUEST_DENIED,
				"Invalid SessionIndexes received");
		sendErrorResponse(req, resp, spConfiguration, details);
	}

	private void handleSLOResponse(HttpServletRequest req,
			HttpServletResponse resp, SAML2Configuration saml2Configuration,
			SAML2SPConfigurationCustom spConfiguration, SLOResponseInfo sloInfo)
			throws IOException {
		String sloStatus = getSLOStatusDetails(sloInfo);
		logger.warn(sloStatus);

		String localeStr = getLocaleStr(req);

		// Without trailing /, jmeter won't do redirect correct
		// it sucks, we have to make this kind of change just to make jmeter
		// work
		String home = saml2Configuration.getHomePage();
		// if(!home.endsWith("/"))
		// home=home+"/";
		resp.sendRedirect(new StringBuilder(home).append("?locale=")
				.append(URLEncoder.encode(localeStr, "utf-8")).append("&")
				.append(new Date().getTime()).toString());
	}

	private String getLocaleStr(HttpServletRequest req) {
		String localeStr = req.getParameter("locale");

		if (StringUtils.isNotBlank(localeStr)) {
			try {
				LocaleUtils.toLocale(localeStr);
			} catch (Exception e) {
				logger.info("locale format failed, locale: " + localeStr, e);
				localeStr = null;
			}
		}

		if (localeStr == null && req.getLocale() != null) {
			localeStr = req.getLocale().toString();
		}

		if (StringUtils.isBlank(localeStr)) {
			localeStr = Locale.getDefault().toString();
		}
		return localeStr;
	}

	private void sendErrorResponse(HttpServletRequest req,
			HttpServletResponse resp, SAML2SPConfiguration spConfig,
			SAML2ErrorResponseDetails details) throws IOException,
			SAML2Exception, SAML2ConfigurationException, ServletException {
		SAML2LogoutResponse logoutResponse = SAML2Authentication.getInstance()
				.createSLOErrorResponse(spConfig, details);
		req.setAttribute(HTTPPostBinding.SAML_RESPONSE,
				SAML2Utils.encodeBase64AsString(logoutResponse.generate()));
		req.setAttribute("destination", logoutResponse.getDestination());
		req.getServletContext().getRequestDispatcher("/waiting.jsp")
				.forward(req, resp);
	}

	private String getSLOStatusDetails(SLOResponseInfo sloResponseInfo) {
		StringBuilder builder = new StringBuilder();
		if (SAML2Constants.STATUS_CODE_TOP_LEVEL_SUCCESS.equals(sloResponseInfo
				.getStatusCode())) {
			builder.append("Success");
		} else {
			builder.append("Error status code: ").append(
					sloResponseInfo.getStatusCode());
			builder.append("; Second level status code: ").append(
					sloResponseInfo.getSecondaryStatusCode());
			builder.append("; Status message: ").append(
					sloResponseInfo.getStatusMessage());
		}
		return builder.toString();
	}

}
