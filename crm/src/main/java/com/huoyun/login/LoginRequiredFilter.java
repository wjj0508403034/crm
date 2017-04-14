package com.huoyun.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.bindings.PAOSDataFactory;
import com.sap.security.saml2.lib.bindings.PAOSHTTPBinding;
import com.sap.security.saml2.lib.common.SAML2DataFactory;
import com.sap.security.saml2.lib.common.SAML2Exception;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.assertions.SAML2IDPEntry;
import com.sap.security.saml2.lib.interfaces.bindings.SOAPHeaderECPRequest;
import com.sap.security.saml2.lib.interfaces.bindings.SOAPHeaderPAOSRequest;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2AuthRequest;

public class LoginRequiredFilter implements Filter{
	private static final Logger logger = LoggerFactory.getLogger(LoginRequiredFilter.class);

    private static String localSessionAttributeName = null;

    private FilterConfig filterConfig;

    private final List<String> excludes = new ArrayList<String>();
    private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String strExcludes = this.filterConfig.getInitParameter("EXCLUDES");
        if (null != strExcludes && !strExcludes.trim().equals(""))
            excludes.addAll(Arrays.asList(strExcludes.split(";")));
        localSessionAttributeName = this.filterConfig.getInitParameter("LOCALE_SESSION_ATTRIBUTE_NAME");
        if (null == localSessionAttributeName) {
            localSessionAttributeName = "locale";
        }
        try {
            saml2SPConfigurationFactory = SAML2SPConfigurationFactory.getInstance();
        } catch (SAML2ConfigurationException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            syncLocale(httpRequest);
            SAML2Configuration saml2Configuration = saml2SPConfigurationFactory.getSAML2Configuration();

            boolean isSkip = false;
            String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
            for (String s : excludes) {
                if (path.startsWith(s)) {
                    isSkip = true;
                    break;
                }
            }
            if (!isSkip && httpRequest.getRequestURI().contains("/saml2/sp/")) {
                isSkip = true;
            }

            if (!saml2Configuration.isSsoEnabled()
                    || httpRequest.getRequestURI().endsWith(saml2Configuration.getLocalACSEndpoint()) || isSkip) {

                if (isSkip) {
                    logger.info("Request {} is skipping SSO authorization.", httpRequest.getRequestURI());
                }

                chain.doFilter(request, response);
                return;
            }

            HttpSession session = httpRequest.getSession(false);
            if (null != session && null != session.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR)) {
                chain.doFilter(request, response);
            } else {
                logger.info("Request is not authorized, require for #SSO authorization. Request URL : {}",
                        request instanceof HttpServletRequest ? ((HttpServletRequest) request).getRequestURI() : null);

                if (PAOSHTTPBinding.isUserAgentSupportECP(httpRequest)) {
                    logger.info("User Agent ECP is supported for request, send PAOSBinding....");
                    sendPAOSBinding(httpRequest, httpResponse, saml2Configuration);
                } else if (httpRequest.getHeader("Accept") != null
                        && httpRequest.getHeader("Accept").contains("json")) {
                    logger.info("Unsupported Accept Header {} from request.", "json");
                    ((HttpServletResponse) response).setStatus(401);
                } else if (httpRequest.getHeader("is-shelljs") != null
                        && Boolean.valueOf(httpRequest.getHeader("is-shelljs"))) {
                    logger.info("POS APP session expired and can not redirect to IDP login page directly");
                    ((HttpServletResponse) response).setStatus(401);
                } else if (httpRequest.getHeader("Accept") == null || httpRequest.getHeader("Accept").contains("html")
                        || httpRequest.getHeader("Accept").contains("*/*")) {
                    logger.info("Supported Accept Header {}, sending HTTPPostBinding....",
                            httpRequest.getHeader("Accept"));
                    sendHTTPPostBinding(httpRequest, httpResponse, saml2Configuration);
                } else {
                    logger.info("Unsupported Accept Header {} from request.", httpRequest.getHeader("Accept"));
                    ((HttpServletResponse) response).setStatus(401);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
    }

    public void sendHTTPPostBinding(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            SAML2Configuration saml2Configuration)
                    throws IOException, SAML2ConfigurationException, SAML2Exception, ServletException {
        SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();
        String acsUrl = getAcsUrl(httpRequest, saml2Configuration);
        String relayState = getRealyState(httpRequest);

        logger.info("Sending SSO HTTPPostBinding, acsUrl {}, relayState {}.", acsUrl, relayState);

        httpResponse.addHeader("MobileNativeSupport", "true");
        SAML2AuthRequest authnRequest = SAML2Authentication.getInstance().createSAML2AuthRequest(spConfiguration,
                saml2Configuration.getDefaultIdPName(), acsUrl);
        httpRequest.setAttribute(HTTPPostBinding.SAML_REQUEST,
                SAML2Utils.encodeBase64AsString(authnRequest.generate()));
        httpRequest.setAttribute(HTTPPostBinding.SAML_RELAY_STATE, relayState);
        httpRequest.setAttribute("destination", authnRequest.getDestination());
        httpRequest.getServletContext().getRequestDispatcher("/sso/waiting.html").forward(httpRequest, httpResponse);
    }

    protected String getRealyState(HttpServletRequest httpRequest) {
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

    public void sendPAOSBinding(HttpServletRequest httpRequest, HttpServletResponse response,
            SAML2Configuration saml2Configuration)
                    throws IOException, SAML2Exception, SAML2ConfigurationException {
        SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();
        String acsUrl = getAcsUrl(httpRequest, saml2Configuration);
        String relayState = getRealyState(httpRequest);

        logger.info("Sending SSO PAOSBinding, acsUrl {}, relayState {}.", acsUrl, relayState);

        SOAPHeaderPAOSRequest paosRequest = PAOSDataFactory.getInstance()
                .createPAOSRequest(httpRequest.getRequestURL().toString());
        SOAPHeaderECPRequest ecpRequest = PAOSDataFactory.getInstance().createECPRequest();
        response.addHeader("MobileNativeSupport", "true");
        SAML2IDPEntry idpEntry = SAML2DataFactory.getInstance()
                .createSAML2IDPEntry(saml2Configuration.getDefaultIdPName());
        idpEntry.setName(saml2Configuration.getDefaultIdPName());
        idpEntry.setLocation(saml2Configuration.getSsoEndpoint());
        List<SAML2IDPEntry> idpEntries = new ArrayList<SAML2IDPEntry>();
        idpEntries.add(idpEntry);
        ecpRequest.setIDPList(idpEntries);
        SAML2AuthRequest samlRequest = SAML2Authentication.getInstance().createSAML2AuthRequest(spConfiguration,
                saml2Configuration.getDefaultIdPName(), acsUrl);
        PAOSHTTPBinding.sendSAML2AuthnRequest(response, paosRequest, ecpRequest, relayState, samlRequest);
        httpRequest.getSession().setAttribute(EndpointsConstatns.SSO_SAML2_ORIGINAL_REQUEST_ID, samlRequest.getID());
    }

    private String getAcsUrl(HttpServletRequest httpRequest, SAML2Configuration saml2Configuration) {
        StringBuilder sb = new StringBuilder();
        sb.append(httpRequest.getScheme()).append("://").append(httpRequest.getServerName());
        if ((httpRequest.getScheme().equals("https") && httpRequest.getServerPort() != 443)
                || (httpRequest.getScheme().equals("http") && httpRequest.getServerPort() != 80)) {
            sb.append(":").append(httpRequest.getServerPort());
        }
        sb.append(httpRequest.getContextPath()).append(saml2Configuration.localACSEndpoint);
        return sb.toString();
    }

    public static void syncLocale(HttpServletRequest httpRequest) {
        Locale locale = null;
        if (StringUtils.isNotBlank(httpRequest.getParameter("locale"))) {
            try {
                locale = LocaleUtils.toLocale(httpRequest.getParameter("locale"));
            } catch (IllegalArgumentException e) {
                logger.warn("Failed to parse locale parameter", e);
            }
        }
        if (null == locale && null != httpRequest.getSession(false)) {
            locale = (Locale) httpRequest.getSession(false).getAttribute(localSessionAttributeName);
            if (locale != null) {
                logger.debug("Locale info from request parameter is {}, set attribute into session.", locale);
                httpRequest.getSession().setAttribute("locale", locale);
            }
        }
    }
}
