package com.huoyun.saml2.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.huoyun.cookie.CookieUtils;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.saml2.SAML2Authentication;
import com.huoyun.saml2.configuration.SAML2Configuration;
import com.huoyun.saml2.configuration.SAML2SPConfigurationCustom;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactoryAware;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutRequest;

public class LogoutTriggerServlet extends HttpServlet implements
		SAML2SPConfigurationFactoryAware {

	private static final long serialVersionUID = 3518368591782236246L;

	private static final Logger logger = LoggerFactory
			.getLogger(LogoutTriggerServlet.class);
	private static Logger auditLogger = LoggerFactory.getLogger("Audit");
	public static final String LOGOUTPARAM = "logout";

	private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

	@Override
	public void setSAML2SPConfigurationFactory(
			SAML2SPConfigurationFactory saml2spConfigurationFactory) {
		this.saml2SPConfigurationFactory = saml2spConfigurationFactory;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			logger.info("[Logout] Handling logout request...");

			// add by leon 2014-10-29
			// remove cookie if have
			CookieUtils.removeCookie(req, resp, CookieUtils.DEFAULT_SBO_PATH,
					CookieUtils.COOKIE_NAME);

			SAML2Configuration saml2Configuration = saml2SPConfigurationFactory
					.getSAML2Configuration();
			SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory
					.createSAML2SPConfiguration();
			HttpSession session = req.getSession(false);
			if (session != null) {
				if (session
						.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR) != null) {
					session.removeAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR);
				}
				SAML2Principal sessionPrincipal = (SAML2Principal) session
						.getAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR);
				if (sessionPrincipal != null) {
					auditLogger.info("User {} logging out",
							sessionPrincipal.getNameId());
					logger.info("[Logout] User {} request to log out.",
							sessionPrincipal.getName());

					SAML2LogoutRequest logoutRequest = SAML2Authentication
							.getInstance().createSLORequest(spConfiguration,
									sessionPrincipal);
					req.setAttribute(HTTPPostBinding.SAML_REQUEST, SAML2Utils
							.encodeBase64AsString(logoutRequest.generate()));
					req.setAttribute("destination",
							logoutRequest.getDestination());
					req.getServletContext()
							.getRequestDispatcher("/waiting.jsp")
							.forward(req, resp);

					logger.info("[Logout] Invalidate http session.");
					session.invalidate();
				} else {
					// add by leon 2014-10-29
					// session != null && sessionprincial == null, which means
					// it may be failed over.
					// Saving saml2 information in cookie will decrease
					// performance of the SFA,
					// so we implement logout by removing "sessionid" from the
					// local.

					String sloEndpoint = saml2Configuration.getSloEndpoint();
					logger.info(
							"[Logout] SAML2Principal in session not found, redirect to {}",
							sloEndpoint);

					CookieUtils.removeCookie(req, resp,
							CookieUtils.DEFAULT_SBO_PATH,
							CookieUtils.JSESSION_COOKIE_NAME);
					CookieUtils.removeCookie(req, resp,
							CookieUtils.DEFAULT_SLD_PATH,
							CookieUtils.JSESSION_COOKIE_NAME);
					CookieUtils.removeCookie(req, resp,
							CookieUtils.DEFAULT_SBO_PATH,
							CookieUtils.RSESSION_COOKIE_NAME);
					CookieUtils.removeCookie(req, resp,
							CookieUtils.DEFAULT_SLD_PATH,
							CookieUtils.RSESSION_COOKIE_NAME);
					resp.sendRedirect(sloEndpoint);
				}
			} else {
				String reDirectURL = saml2Configuration.getHomePage() + "?"
						+ LOGOUTPARAM + "=true";
				logger.info("[Logout] Session not found, redirect to {}",
						reDirectURL);

				resp.sendRedirect(reDirectURL);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
