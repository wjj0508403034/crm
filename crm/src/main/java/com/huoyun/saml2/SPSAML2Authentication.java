package com.huoyun.saml2;

import java.security.PrivateKey;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.sap.security.saml2.cfg.enums.SAML2Binding;
import com.sap.security.saml2.cfg.enums.SignatureOption;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.cfg.interfaces.SAML2SPConfiguration;
import com.sap.security.saml2.cfg.interfaces.read.SAML2Endpoint;
import com.sap.security.saml2.cfg.interfaces.read.SAML2LocalSP;
import com.sap.security.saml2.cfg.interfaces.read.SAML2TrustedIdP;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.common.SAML2Constants;
import com.sap.security.saml2.lib.common.SAML2ErrorResponseDetails;
import com.sap.security.saml2.lib.common.SAML2Exception;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2AuthRequest;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutRequest;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutResponse;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2ProtocolToken;
import com.sap.security.saml2.sp.sso.SLORequestInfo;
import com.sap.tc.logging.Location;

public class SPSAML2Authentication {
	private static final Location LOCATION = Location
			.getLocation(SPSAML2Authentication.class);
	private static final SPSAML2Authentication INSTANCE = new SPSAML2Authentication();

	private SPSAML2Authentication() {
	}

	/**
	 * Provides singleton access to {@link SPSAML2Authentication}.
	 * 
	 * @return
	 */
	public static SPSAML2Authentication getInstance() {
		return INSTANCE;
	}

	public SAML2AuthRequest createSAML2AuthRequest(
			SAML2SPConfiguration configuration, String trustedIdPName,
			String assertionConsumerServiceUrl)
			throws SAML2ConfigurationException, SAML2Exception {
		// Get local service provider configuration
		SAML2LocalSP localSP = configuration.getLocalSP();

		// Get the trusted identity provider configuration
		SAML2TrustedIdP trustedIdP = configuration
				.getTrustedIdP(trustedIdPName);
		if (trustedIdP == null) {
			throw new SAML2ConfigurationException(
					"There is no configuration for trusted Identity Provider: "
							+ trustedIdPName);
		}

		// Get the SSO endpoint to which the request will be sent
		SAML2Endpoint idpSSOEndpoint = trustedIdP
				.getDefaultSingleSignOnEndpoint();
		if (idpSSOEndpoint == null) {
			throw new SAML2ConfigurationException(
					"Default SSO endpoint is not configured for trusted IdP: "
							+ trustedIdPName);
		}

		// Get the SSO endpoint location URL
		String idpSSOLocation = idpSSOEndpoint.getLocation();
		if (idpSSOLocation == null || idpSSOLocation.length() < 1) {
			throw new SAML2ConfigurationException(
					"Empty location is configured in SSO endpoint for IdP: "
							+ trustedIdPName);
		}

		// Create the authentication request
		SAML2AuthRequest authnRequest = AuthnRequestFactory.createAuthnRequest(
				localSP, trustedIdP, idpSSOLocation,
				assertionConsumerServiceUrl, null);

		// Check signature requirements and sign authentication request if
		// configured
		if (!trustedIdP.isToSignAuthnRequests().equals(SignatureOption.NEVER)) {
			PrivateKey privateKey = localSP.getPrivateKeyForSignature();
			if (privateKey == null) {
				throw new SAML2ConfigurationException(
						"Service Provider cannot read its private key for signature from configuration.");
			}

			authnRequest.sign(privateKey);
			LOCATION.debugT("Authentication request successfully signed.");
		}
		return authnRequest;
	}

	public SAML2LogoutRequest createSLORequest(
			SAML2SPConfiguration configuration, SAML2Principal principal)
			throws SAML2Exception, SAML2ConfigurationException {
		String trustedIdPName = principal.getIdPName();
		if (trustedIdPName == null || trustedIdPName.length() < 1) {
			throw new SAML2Exception(
					"SLO request message could not be created, because SAML2Principal does not contain trusted Identity Provider name.");
		}
		SAML2LocalSP localSP = configuration.getLocalSP();
		SAML2TrustedIdP trustedIdP = configuration
				.getTrustedIdP(trustedIdPName);

		if (trustedIdP == null) {
			throw new SAML2ConfigurationException(
					"There is no configuration for trusted Identity Provider with name:"
							+ trustedIdPName);
		}

		SAML2Endpoint idpSLOEndpoint = getSLOEndpoint(trustedIdP,
				SAML2Binding.HTTP_REDIRECT_BINDING);
		if (idpSLOEndpoint == null) {
			throw new SAML2ConfigurationException(
					"SLO endpoint is not configured for IdP: "
							+ trustedIdP.getName());
		}
		String idpSLOLocation = idpSLOEndpoint.getLocation();
		if (idpSLOLocation == null || idpSLOLocation.length() < 1) {
			throw new SAML2ConfigurationException(
					"Empty location is configured in SLO endpoint for IdP: "
							+ trustedIdP.getName());
		}

		SAML2LogoutRequest logoutRequest;
		try {
			logoutRequest = LogoutRequestFactory.createLogoutRequest(localSP,
					trustedIdP, principal, idpSLOLocation, null);
		} catch (LoginException e) {
			throw new SAML2Exception("Could not create SAML2 LogoutRequest.", e);
		}

		signSLOMessage(localSP, trustedIdP, logoutRequest);

		return logoutRequest;

	}

	public SAML2LogoutResponse createSLOResponse(
			SAML2SPConfiguration configuration, SLORequestInfo sloRequestInfo)
			throws SAML2Exception, SAML2ConfigurationException {

		String trustedIdPName = sloRequestInfo.getIssuer();
		if (trustedIdPName == null || trustedIdPName.length() < 1) {
			throw new SAML2Exception(
					"SLO request message could not be created, because sloRequestInfo does not contain issuer name.");
		}
		String statusCode = SAML2Constants.STATUS_CODE_TOP_LEVEL_SUCCESS;
		SAML2LogoutResponse logoutResponse = LogoutResponseFactory
				.createLogoutResponse(configuration, trustedIdPName,
						SAML2Binding.HTTP_POST_BINDING, sloRequestInfo.getId(),
						statusCode, null, null);

		SAML2TrustedIdP trustedIdP = configuration
				.getTrustedIdP(trustedIdPName);
		if (trustedIdP == null) {
			throw new SAML2ConfigurationException(
					"There is no configuration for trusted Identity Provider with name:"
							+ trustedIdPName);
		}

		signSLOMessage(configuration.getLocalSP(), trustedIdP, logoutResponse);

		return logoutResponse;

	}

	private SAML2Endpoint getSLOEndpoint(SAML2TrustedIdP trustedIdP,
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

	public SAML2LogoutResponse createSLOErrorResponse(
			SAML2SPConfiguration configuration,
			SAML2ErrorResponseDetails errorDetails) throws SAML2Exception,
			SAML2ConfigurationException {

		String trustedIdPName = errorDetails.getOriginalRequestIssuer();
		if (trustedIdPName == null || trustedIdPName.length() < 1) {
			throw new SAML2Exception(
					"SLO error response message could not be created, because errorDetails does not contain issuer name of the original request.");
		}

		SAML2LogoutResponse logoutResponse = LogoutResponseFactory
				.createLogoutResponse(configuration, trustedIdPName,
						SAML2Binding.HTTP_POST_BINDING,
						errorDetails.getOriginalRequestId(),
						errorDetails.getStatusCode(),
						errorDetails.getSecondLevelStatusCode(),
						errorDetails.getStatusMsg());

		SAML2TrustedIdP trustedIdP = configuration
				.getTrustedIdP(trustedIdPName);
		if (trustedIdP == null) {
			throw new SAML2ConfigurationException(
					"There is no configuration for trusted Identity Provider with name:"
							+ trustedIdPName);
		}

		signSLOMessage(configuration.getLocalSP(), trustedIdP, logoutResponse);

		return logoutResponse;

	}

	private void signSLOMessage(SAML2LocalSP localSP,
			SAML2TrustedIdP trustedIdP, SAML2ProtocolToken logoutMessage)
			throws SAML2Exception, SAML2ConfigurationException {
		SignatureOption signSLOMessages = trustedIdP
				.isToSignSingleLogoutMessages();
		if (signSLOMessages == SignatureOption.ALWAYS
				|| signSLOMessages == SignatureOption.FRONT_CHANNEL_ONLY) {
			PrivateKey privateKey = localSP.getPrivateKeyForSignature();
			if (localSP.isToIncludeCertInSignature()) {
				logoutMessage.sign(privateKey,
						trustedIdP.getCertificateForSignature());
			} else {
				logoutMessage.sign(privateKey, null);
			}
		}
	}
}
