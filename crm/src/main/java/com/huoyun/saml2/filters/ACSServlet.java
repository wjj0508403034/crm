package com.huoyun.saml2.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.saml2.configuration.SAML2Configuration;
import com.huoyun.saml2.configuration.SAML2SPConfigurationCustom;
import com.huoyun.saml2.configuration.SAML2SPConfigurationFactory;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.commons.Attribute;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.bindings.HTTPPostBinding;
import com.sap.security.saml2.lib.bindings.PAOSHTTPBinding;
import com.sap.security.saml2.lib.bindings.SOAPHTTPBinding;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.bindings.ECPResponseMessage;
import com.sap.security.saml2.sp.sso.SAML2Authentication;

public class ACSServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ACSServlet.class);

    private SAML2SPConfigurationFactory saml2SPConfigurationFactory;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            saml2SPConfigurationFactory = SAML2SPConfigurationFactory.getInstance();
        } catch (SAML2ConfigurationException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            SAML2Configuration saml2Configuration = saml2SPConfigurationFactory.getSAML2Configuration();
            SAML2SPConfigurationCustom spConfiguration = saml2SPConfigurationFactory.createSAML2SPConfiguration();
            if (null != req.getContentType() && req.getContentType().contains(SOAPHTTPBinding.SOAP_MIME_TYPE)) {
                ECPResponseMessage ecpResponse = PAOSHTTPBinding.extractECPResponseMessage(req);
                String acs = getAcsUrl(req, saml2Configuration);
                String response = ecpResponse.getSAML2Response().getXMLRepresentation().replaceAll("[\\r\\n]*", "");
                SAML2Principal saml2Principal = SAML2Authentication.getInstance().validateResponse(spConfiguration,
                        response, null, acs);

                changeSessionId(req);

                LOGGER.info("SSO authorization succeed for user {}.", saml2Principal.getName());

                req.getSession().setAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR, saml2Principal.getName());
                req.getSession().setAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR, saml2Principal);

                addSessionIndex2HttpSession4SOAPType(req, saml2Principal);

                resp.setStatus(200);
            } else {

                String acs = getAcsUrl(req, saml2Configuration);
                SAML2Principal saml2Principal = SAML2Authentication.getInstance().validateResponse(spConfiguration,
                        SAML2Utils.decodeBase64AsString(req.getParameter(HTTPPostBinding.SAML_RESPONSE)), null, acs);

                changeSessionId(req);

                LOGGER.info("SSO authorization succeed for user {}.", saml2Principal.getName());

                req.getSession().setAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR, saml2Principal.getName());
                req.getSession().setAttribute(EndpointsConstatns.SSO_PRINCIPAL_SESSION_ATTR, saml2Principal);
                req.getSession().setAttribute(EndpointsConstatns.SSO_SESSION_INDEX,
                        req.getParameter(EndpointsConstatns.SSO_SESSION_INDEX));
                String url = req.getParameter(HTTPPostBinding.SAML_RELAY_STATE);

                resp.sendRedirect(saml2Configuration.getHomePage());
            }
        } catch (Exception e) {
            LOGGER.error("ERROR on ACSServlet", e);
            throw new ServletException(e);
        }
    }

    /**
     * @param req
     * @param saml2Principal
     */
    private void addSessionIndex2HttpSession4SOAPType(HttpServletRequest req, SAML2Principal saml2Principal) {
        String session_index = null;
        Set<Attribute> attributes = saml2Principal.getAttributes();
        if (null != attributes && !CollectionUtils.isEmpty(attributes)) {
            for (Attribute attr : attributes) {
                if (EndpointsConstatns.SSO_SESSION_INDEX.equals(attr.getName())) {
                    List<String> values = attr.getValues();
                    if (null != values && values.size() > 0) {
                        session_index = values.get(0);
                        break;
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(session_index)) {
            req.getSession().setAttribute(EndpointsConstatns.SSO_SESSION_INDEX, session_index);
        }
    }

    /**
     * Change session id in order to prevent 'session-fixation' attacks
     * 
     * @param req
     *            http request
     */
    private HttpSession changeSessionId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            return req.getSession();
        }

        Map<String, Object> sessionValues = new HashMap<String, Object>();

        Enumeration<String> keys = session.getAttributeNames();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Object value = session.getAttribute(key);
            sessionValues.put(key, value);
        }

        session.invalidate();

        session = req.getSession();

        Set<Map.Entry<String, Object>> entrys = sessionValues.entrySet();
        for (Map.Entry<String, Object> entry : entrys) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        return session;
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

}
