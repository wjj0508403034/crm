package com.huoyun.saml2;

import java.util.Date;

import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.LoginException;

import com.sap.security.saml2.commons.AuditData;
import com.sap.security.saml2.commons.CommonSAML2Utils;
import com.sap.security.saml2.commons.IdPProxyContext;
import com.sap.security.saml2.lib.attributes.AttributeValues;
import com.sap.security.saml2.sp.sso.exception.BadCredentialsException;
import com.sap.security.saml2.sp.sso.exception.LoginFailedException;
import com.sap.security.saml2.sp.sso.exception.NameIdMappingFailedException;
import com.sap.security.saml2.sp.sso.exception.NameIdMappingNoSuchUserException;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;

public class LoginResult {
	private static final Location LOCATION = Location
			.getLocation(LoginResult.class);

	static final String RESUMING_AUTHENTICATION_MSG = "Resuming SAML2 authentication based on already validated SAML2 token in previous authentication attempt.";

	static final byte STATUS_CODE_CHALLENGE = 0;
	static final byte STATUS_CODE_AUTHENTICATION = 1;
	static final byte STATUS_CODE_FAILURE = 2;
	static final byte STATUS_CODE_REAUTHENTICATION = 3;
	static final byte STATUS_CODE_FEDERATION = 4;
	static final byte STATUS_CODE_SSO = 5;
	static final byte STATUS_CODE_IDPDISCOVERY = 6;
	static final byte STATUS_CODE_MANUAL_IDP_SELECTION = 7;

	// The local user account name
	private String localUser;

	// The Subject Name ID received in assertion
	private String principalNameId;

	// The Subject Name ID Format received in assertion
	private String principalNameIdFormat;

	// The Subject Name ID SPProvidedID received in assertion
	private String nameIdSPProvidedID;

	// The Subject Name ID SPNameQualifier received in assertion
	private String subjectNameIdSPNameQualifier;

	// The Subject Name ID Qualifier received in assertion
	private String subjectNameIdNameQualifier;

	// The attributes received in the Assertion
	private AttributeValues assertionAttributes;

	// The trusted IdP name that authenticated the user
	private String idpName;

	// The session index received from the IdP
	private String sessionIndex;

	// The client context alias
	private String clusterWideClientID;

	// Session expiration time
	private Date sessionNotOnOrAfter;

	// The NotOnOrAfter of the chosen validated SubjectConfirmation
	// plus the configured clock skew tolerance
	private Date subjectConfirmationNotOnOrAfter;

	// The authentication context received from the IdP
	private String authnContext;

	// The received SAML2 token ID
	private String tokenId;

	// The flag for necessary interactive persistent federation
	private boolean federationRequired = false;

	// The login status code. Default = failure
	private byte loginStatus = STATUS_CODE_FAILURE;

	// Context necessary for saving IdP proxy related information
	private IdPProxyContext idpProxyContext;

	// Holder of data that will be logged as audit in the login table
	private AuditData auditData = new AuditData();

	// Flag that tells whether we are resuming authentication or not
	private boolean hasLoginResultInSession;

	// Flag that tells whether the SAML2 response received was signed
	private boolean isSAML2ResponseSigned;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LoginResult: \nlocalUser=").append(localUser);
		sb.append("\nprincipalNameId=").append(principalNameId);
		sb.append("\nprincipalNameIdFormat=").append(principalNameIdFormat);
		sb.append("\nnameIdSPProvidedID=").append(nameIdSPProvidedID);
		sb.append("\nsubjectNameIdSPNameQualifier=").append(
				subjectNameIdSPNameQualifier);
		sb.append("\nsubjectNameIdNameQualifier=").append(
				subjectNameIdNameQualifier);
		sb.append("\nassertionAttributes=").append(assertionAttributes);
		sb.append("\nidpName=").append(idpName);
		sb.append("\nsessionIndex=").append(sessionIndex);
		sb.append("\nclusterWideClientID=").append(clusterWideClientID);
		sb.append("\nsessionNotOnOrAfter=").append(sessionNotOnOrAfter);
		sb.append("\nsubjectConfirmationNotOnOrAfter=").append(
				subjectConfirmationNotOnOrAfter);
		sb.append("\nauthnContext=").append(authnContext);
		sb.append("\ntokenId=").append(tokenId);
		sb.append("\nloginStatus=").append(loginStatus);
		sb.append("\nfederationRequired=").append(federationRequired);
		sb.append("\nIdP Proxy Context=").append(idpProxyContext);
		sb.append("\nauditData=").append(auditData);
		sb.append("\nisSAML2ResponseSigned=").append(isSAML2ResponseSigned);
		return sb.toString();
	}

	public String getLocalUser() {
		return localUser;
	}

	void setLocalUser(String localUser) {
		this.localUser = localUser;
	}

	String getSessionIndex() {
		return sessionIndex;
	}

	void setSessionIndex(String sessionIndex) {
		this.sessionIndex = sessionIndex;
	}

	String getPrincipalNameId() {
		return principalNameId;
	}

	void setPrincipalNameId(String principalNameId) {
		this.principalNameId = principalNameId;
	}

	String getPrincipalNameIdFormat() {
		return principalNameIdFormat;
	}

	void setPrincipalNameIdFormat(String principalNameIdFormat) {
		this.principalNameIdFormat = principalNameIdFormat;
	}

	String getNameIdSPProvidedId() {
		return nameIdSPProvidedID;
	}

	void setNameIdSPProvidedId(String nameIdSPProvidedID) {
		this.nameIdSPProvidedID = nameIdSPProvidedID;
	}

	String getSubjectNameIdSPNameQualifier() {
		return subjectNameIdSPNameQualifier;
	}

	void setSubjectNameIdSPNameQualifier(String subjectNameIdSPNameQualifier) {
		this.subjectNameIdSPNameQualifier = subjectNameIdSPNameQualifier;
	}

	String getSubjectNameIdNameQualifier() {
		return subjectNameIdNameQualifier;
	}

	void setSubjectNameIdNameQualifier(String subjectNameQualifier) {
		this.subjectNameIdNameQualifier = subjectNameQualifier;
	}

	AttributeValues getAssertionAttributes() {
		return assertionAttributes;
	}

	void setAssertionAttributes(AttributeValues assertionAttributes) {
		this.assertionAttributes = assertionAttributes;
	}

	String getIdpName() {
		return idpName;
	}

	void setIdpName(String idpName) {
		this.idpName = idpName;
	}

	public String getAuthenticationContext() {
		return authnContext;
	}

	void setAuthenticationContext(String authnContext) {
		this.authnContext = authnContext;
	}

	String getTokenId() {
		return tokenId;
	}

	void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	byte getLoginStatus() {
		return loginStatus;
	}

	void setLoginStatus(byte loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * Thrown only when resuming authentication.
	 */
	NameIdMappingNoSuchUserException getMappingFailedException() {
		return new NameIdMappingNoSuchUserException(RESUMING_AUTHENTICATION_MSG);
	}

	void throwCredentialNotFoundException(String message)
			throws CredentialNotFoundException {
		String auditMessage = null;
		if (auditData.getAssertion() != null) {
			auditMessage = CommonSAML2Utils.getRejectedAssertionAuditString(
					auditData, message);
		} else {
			auditMessage = message;
		}

		loginStatus = STATUS_CODE_CHALLENGE;
		throw new CredentialNotFoundException(auditMessage);
	}

	void throwIdPDiscoveryException(String message) throws LoginException {
		loginStatus = STATUS_CODE_IDPDISCOVERY;
		throw new LoginException(message);
	}

	void throwManualIdPSelectionException(String message) throws LoginException {
		loginStatus = STATUS_CODE_MANUAL_IDP_SELECTION;
		throw new LoginException(message);
	}

	void throwLoginFailedException(String message) throws LoginFailedException {
		String auditMessage = getAuditMessage(message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new LoginFailedException(auditMessage);
	}

	void throwLoginFailedException(String message, Throwable cause)
			throws LoginFailedException {
		LOCATION.traceThrowableT(Severity.DEBUG, message, cause);

		String auditMessage = getAuditMessage(message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new LoginFailedException(auditMessage, cause);
	}

	void throwBadCredentialException(String message)
			throws BadCredentialsException {
		String auditMessage = getAuditMessage(message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new BadCredentialsException(auditMessage);
	}

	void throwBadCredentialException(String message, Throwable cause)
			throws BadCredentialsException {
		String auditMessage = getAuditMessage(message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new BadCredentialsException(auditMessage, cause);
	}

	void throwInteractiveFederationRequired(String message)
			throws NameIdMappingNoSuchUserException {
		String auditMessage = CommonSAML2Utils.getSuccessAssertionAuditString(
				auditData, message);
		loginStatus = STATUS_CODE_FEDERATION;
		throw new NameIdMappingNoSuchUserException(auditMessage);
	}

	void throwNameIDMappingNoSuchUserException(String message)
			throws NameIdMappingNoSuchUserException {
		String auditMessage = CommonSAML2Utils.getRejectedAssertionAuditString(
				auditData, message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new NameIdMappingNoSuchUserException(auditMessage);
	}

	void throwNameIDMappingFailedException(String message, Throwable cause)
			throws NameIdMappingFailedException {
		LOCATION.traceThrowableT(Severity.DEBUG, message, cause);

		String auditMessage = CommonSAML2Utils.getRejectedAssertionAuditString(
				auditData, message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new NameIdMappingFailedException(auditMessage, cause);
	}

	void throwNameIDMappingFailedException(String message)
			throws NameIdMappingFailedException {
		String auditMessage = CommonSAML2Utils.getRejectedAssertionAuditString(
				auditData, message);
		loginStatus = STATUS_CODE_FAILURE;
		throw new NameIdMappingFailedException(auditMessage);
	}

	String getClusterWideClientID() {
		return clusterWideClientID;
	}

	void setClusterWideClientID(String clientId) {
		this.clusterWideClientID = clientId;
	}

	Date getSessionNotOnOrAfter() {
		return sessionNotOnOrAfter;
	}

	void setSessionNotOnOrAfter(Date sessionNotOnOrAfter) {
		this.sessionNotOnOrAfter = sessionNotOnOrAfter;
	}

	/**
	 * Returns NotOnOrAfter of the chosen validated SubjectConfirmation + the
	 * configured clock skew tolerance
	 * 
	 * @return the subjectConfirmationNotOnOrAfter
	 */
	Date getSubjectConfirmationNotOnOrAfter() {
		return subjectConfirmationNotOnOrAfter;
	}

	/**
	 * Set the validity of the chosen SubjectConfirmation Note that this
	 * validity must include the configured clock skew tolerance
	 * 
	 * @param subjectConfirmationNotOnOrAfter
	 */
	void setSubjectConfirmationNotOnOrAfter(Date subjectConfirmationNotOnOrAfter) {
		this.subjectConfirmationNotOnOrAfter = subjectConfirmationNotOnOrAfter;
	}

	boolean isFederationRequired() {
		return federationRequired;
	}

	void setFederationRequired(boolean federationRequired) {
		this.federationRequired = federationRequired;
	}

	IdPProxyContext getIdpProxyContext() {
		return idpProxyContext;
	}

	void setIdpProxyContext(IdPProxyContext idpProxyContext) {
		this.idpProxyContext = idpProxyContext;
	}

	AuditData getAuditData() {
		return auditData;
	}

	private String getAuditMessage(String message) {
		String auditMessage = null;
		if (auditData.getAssertion() != null) {
			auditMessage = CommonSAML2Utils.getRejectedAssertionAuditString(
					auditData, message);
		} else if (auditData.getSaml2ResponseBase() != null) {
			auditMessage = CommonSAML2Utils.getRejectedResponseAuditString(
					auditData, message);
		} else {
			auditMessage = message;
		}
		return auditMessage;
	}

	boolean hasLoginResultInSession() {
		return hasLoginResultInSession;
	}

	void setLoginResultInSession(boolean hasLoginResultInSession) {
		this.hasLoginResultInSession = hasLoginResultInSession;
	}

	boolean isSAML2ResponseSigned() {
		return isSAML2ResponseSigned;
	}

	void setSAML2ResponseSigned(boolean isSAML2ResponseSigned) {
		this.isSAML2ResponseSigned = isSAML2ResponseSigned;
	}
}
