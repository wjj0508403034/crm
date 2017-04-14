package com.huoyun.saml2;

import java.util.Date;
import java.util.List;

import com.sap.security.saml2.cfg.enums.SAML2Binding;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.cfg.interfaces.SAML2SPConfiguration;
import com.sap.security.saml2.cfg.interfaces.read.SAML2Endpoint;
import com.sap.security.saml2.cfg.interfaces.read.SAML2LocalSP;
import com.sap.security.saml2.cfg.interfaces.read.SAML2TrustedIdP;
import com.sap.security.saml2.lib.common.SAML2DataFactory;
import com.sap.security.saml2.lib.common.SAML2Exception;
import com.sap.security.saml2.lib.common.SAML2ProtocolFactory;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.assertions.SAML2NameID;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutResponse;

public class LogoutResponseFactory {
	static SAML2LogoutResponse createLogoutResponse(SAML2LocalSP localSP,
			String idpSLOLocation, String inResponseTo, String statusCode,
			String secondLevelStatusCode, String statusMessage)
			throws SAML2Exception {

		SAML2LogoutResponse logoutResponse = SAML2ProtocolFactory.getInstance()
				.createLogoutResponse(SAML2Utils.generateUUID(), statusCode,
						new Date());

		// Set second level status code
		logoutResponse.setSecondLevelStatusCode(secondLevelStatusCode);

		// Set Issuer
		SAML2NameID nameID = SAML2DataFactory.getInstance().createSAML2NameID(
				localSP.getName());
		logoutResponse.setIssuer(nameID);

		// Set InResponseTo
		logoutResponse.setInResponseTo(inResponseTo);

		logoutResponse.setDestination(idpSLOLocation);

		// Set Status Message
		logoutResponse.setStatusMessage(statusMessage);

		return logoutResponse;
	}

	static SAML2LogoutResponse createLogoutResponse(SAML2SPConfiguration cfg,
			String trustedIdPName, SAML2Binding binding, String inResponseTo,
			String statusCode, String secondLevelStatusCode,
			String statusMessage) throws SAML2Exception,
			SAML2ConfigurationException {

		SAML2LocalSP localSP = cfg.getLocalSP();
		SAML2TrustedIdP trustedIdP = cfg.getTrustedIdP(trustedIdPName);

		SAML2Endpoint idpSLOEndpoint = getSLOEndpoint(trustedIdP, binding);

		String idpSLOLocation = idpSLOEndpoint.getResponseLocation();
		if (idpSLOLocation == null || idpSLOLocation.length() < 1) {
			idpSLOLocation = idpSLOEndpoint.getLocation();
			if (idpSLOLocation == null || idpSLOLocation.length() < 1) {
				throw new SAML2ConfigurationException(
						"Configured SLO endpoint for IdP: "
								+ trustedIdP.getName()
								+ " does not contain ResponseLocation or Location");
			}
		}

		return createLogoutResponse(localSP, idpSLOLocation, inResponseTo,
				statusCode, secondLevelStatusCode, statusMessage);
	}

	private static SAML2Endpoint getSLOEndpoint(SAML2TrustedIdP trustedIdP,
			SAML2Binding binding) throws SAML2ConfigurationException {
		List<SAML2Endpoint> sloEndpoints = trustedIdP
				.getSingleLogoutEndpoints(binding);
		if (sloEndpoints == null || sloEndpoints.size() < 1) {
			throw new SAML2ConfigurationException(
					"SLO request message could not be created, because there are not any SLO endpoints configured for binding: "
							+ binding);
		}

		// get the first SLO endpoint
		SAML2Endpoint idpSLOEndpoint = sloEndpoints.get(0);
		for (SAML2Endpoint endpoint : sloEndpoints) {
			if (endpoint != null && endpoint.isDefault()) {
				// get the default one
				idpSLOEndpoint = endpoint;
				break;
			}
		}
		return idpSLOEndpoint;
	}
}
