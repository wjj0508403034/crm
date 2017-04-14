package com.huoyun.saml2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.sap.security.saml2.cfg.enums.ResponseRequirementType;
import com.sap.security.saml2.cfg.enums.SAML2Binding;
import com.sap.security.saml2.cfg.enums.SAML2NameIdFormat;
import com.sap.security.saml2.cfg.interfaces.read.SAML2LocalSP;
import com.sap.security.saml2.cfg.interfaces.read.SAML2TrustedIdP;
import com.sap.security.saml2.commons.AuthnContext;
import com.sap.security.saml2.commons.AuthnContextList;
import com.sap.security.saml2.lib.common.SAML2DataFactory;
import com.sap.security.saml2.lib.common.SAML2Exception;
import com.sap.security.saml2.lib.common.SAML2ProtocolFactory;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.assertions.SAML2NameID;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2AuthRequest;
import com.sap.security.saml2.sp.sso.AuthenticationRequirements;
import com.sap.tc.logging.Location;

public class AuthnRequestFactory {
	private static final Location LOCATION = Location
			.getLocation(AuthnRequestFactory.class);

	protected static SAML2AuthRequest createAuthnRequest(SAML2LocalSP localSP,
			SAML2TrustedIdP trustedIdP, String idpSSOUrl, String acsUrl,
			AuthenticationRequirements authnRequirements) throws SAML2Exception {
		// Create authentication request
		Date now = Calendar.getInstance().getTime();
		String requestId = SAML2Utils.generateUUID();
		SAML2AuthRequest samlRequest = SAML2ProtocolFactory.getInstance()
				.createAuthnRequest(requestId, now);
		SAML2NameID nameID = SAML2DataFactory.getInstance().createSAML2NameID(
				localSP.getName());
		samlRequest.setIssuer(nameID);
		samlRequest.setDestination(idpSSOUrl);

		setAuthRequestResponseBindings(trustedIdP, samlRequest, acsUrl);

		// Set Authentication Contexts
		List<String> contextNames = trustedIdP.getAuthenticationContextNames();
		AuthnContextList configuredAuthnContexts = new AuthnContextList();

		if (contextNames != null && !contextNames.isEmpty()) {
			for (String contextName : contextNames) {
				// TODO: check whether isInteractive, isHTTPS, isHTTP, isCustom
				// should be configurable
				// new AuthnContext(name, alias, isInteractive, isHTTPS, isHTTP,
				// isCustom)

				AuthnContext newContext = new AuthnContext(contextName);
				configuredAuthnContexts.add(newContext);
			}
		}

		if (configuredAuthnContexts.isEmpty()) {
			if (LOCATION.beDebug()) {
				LOCATION.debugT("Authentication Contexts are not configured for the Service Provider application.");
			}
		} else {
			samlRequest
					.setRequestedAuthnContextClassRefs(configuredAuthnContexts
							.getNames());
			if (LOCATION.beDebug()) {
				LOCATION.debugT(
						"Authentication Contexts set in the SAML2AuthRequest: {0}",
						new Object[] { configuredAuthnContexts.getNames() });
			}
		}

		// Set NameID Policy Format
		SAML2NameIdFormat nameIdFormat = trustedIdP.getDefaultNameIdFormat();
		if (nameIdFormat != null) {
			samlRequest.setNameIDPolicyFormat(nameIdFormat.getName());
			if (LOCATION.beDebug()) {
				LOCATION.debugT(
						"Name ID Format [{0}] set in authentication request.",
						new Object[] { nameIdFormat.getName() });
			}
		}

		if (trustedIdP.getRequestedNameIdPolicySPNameQualifier() != null) {
			samlRequest.setNameIDPolicySPNameQualifier(trustedIdP
					.getRequestedNameIdPolicySPNameQualifier());
		}

		if (trustedIdP.getRequestedNameIdPolicyAllowCreate() != null) {
			samlRequest.setNameIDPolicyAllowCreate(trustedIdP
					.getRequestedNameIdPolicyAllowCreate());
		}

		if (authnRequirements != null) {
			// Overwrite the configuration with the specified authentication
			// requirements

			if (authnRequirements.getNameIdPolicyFormat() != null) {
				samlRequest.setNameIDPolicyFormat(authnRequirements
						.getNameIdPolicyFormat());
			}

			if (authnRequirements.getNameIdPolicySPNameQualifier() != null) {
				samlRequest.setNameIDPolicySPNameQualifier(authnRequirements
						.getNameIdPolicySPNameQualifier());
			}

			if (authnRequirements.getNameIdPolicyAllowCreate() != null) {
				samlRequest.setNameIDPolicyAllowCreate(authnRequirements
						.getNameIdPolicyAllowCreate());
			}

			if (authnRequirements.getForceAuthentication() != null) {
				samlRequest.setForceAuthn(authnRequirements
						.getForceAuthentication());
			}

			if (authnRequirements.getPassiveAuthentication() != null) {
				samlRequest.setPassive(authnRequirements
						.getPassiveAuthentication());
			}

			if (authnRequirements.getConfiguredAuthnContexts() != null) {
				if (samlRequest.getRequestedAuthnContextClassRefs() != null) {
					samlRequest.getRequestedAuthnContextClassRefs().clear();
				}
				samlRequest.setRequestedAuthnContextClassRefs(authnRequirements
						.getConfiguredAuthnContexts().getNames());
			}

			if (authnRequirements.getSaml2Scoping() != null) {
				samlRequest.setScoping(authnRequirements.getSaml2Scoping());
			}
		}

		return samlRequest;
	}

	/**
	 * Sets the authentication request response bindings
	 * 
	 * @param samlRequest
	 * @param applicationUrl
	 * @throws LoginException
	 */
	private static void setAuthRequestResponseBindings(
			SAML2TrustedIdP trustedIdP, SAML2AuthRequest samlRequest,
			String acsUrl) {
		final String METHOD_NAME = "setAuthRequestResponseBindings";
		if (LOCATION.bePath()) {
			LOCATION.entering(METHOD_NAME);
		}

		ResponseRequirementType responseRequirementType = trustedIdP
				.getResponseRequirementType();

		// Set Assertion Consumer Service index if configured
		// acsIndex -> index
		// acsUrl - getRequestURL useBinding
		// custom - getURL
		// IDPDefault - nothing to set

		if (acsUrl != null) {

			SAML2Binding responseBinding = trustedIdP
					.getResponseRequirementBinding();
			if (responseBinding != null) {
				// Set response binding
				samlRequest.setProtocolBinding(responseBinding.getName());

				if (LOCATION.beDebug()) {
					LOCATION.debugT(
							"SSO response binding set in the request: {0}",
							new Object[] { responseBinding.getName() });
				}
			} else {
				LOCATION.debugT("No response binding configured for the trusted Identity Provider.");
			}

			// Set Assertion Consumer Service URL
			samlRequest.setAssertionConsumerServiceURL(acsUrl);

			if (LOCATION.beDebug()) {
				LOCATION.debugT(
						"AssertionConsumerService URL set in the authentication request: {0}",
						new Object[] { acsUrl });
			}
		} else {

			if (ResponseRequirementType.ASSERTION_CONSUMER_URL
					.equals(responseRequirementType)) {
				throw new IllegalArgumentException(
						"Assertion Consumer Service URL could not be null as in the configuration the requirement type is set to Assertion Consumer URL.");

			} else if (ResponseRequirementType.ASSERTION_CONSUMER_INDEX
					.equals(responseRequirementType)) {

				int acsIndex = trustedIdP
						.getResponseRequirementAssertionConsumerIndex();
				samlRequest.setAssertionConsumerServiceIndex(acsIndex);

				if (LOCATION.beDebug()) {
					LOCATION.debugT(
							"AssertionConsumerService Index set in the Authentication Request: [{0}]",
							new Object[] { acsIndex });
				}
			} else if (ResponseRequirementType.IDP_DEFAULT
					.equals(responseRequirementType)) {
				if (LOCATION.beDebug()) {
					LOCATION.debugT("AssertionConsumerService response requrement type set to IDP default");
				}
			} else {
				LOCATION.debugT("No requirement type is specified so idp default setting will be used.");
			}
		}
	}
}
