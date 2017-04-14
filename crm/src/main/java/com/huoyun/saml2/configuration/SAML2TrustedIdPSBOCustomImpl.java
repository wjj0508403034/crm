package com.huoyun.saml2.configuration;

import java.security.cert.X509Certificate;
import java.util.Collection;

import com.sap.security.saml2.cfg.enums.SignatureOption;
import com.sap.security.saml2.cfg.interfaces.read.SAML2Endpoint;

public class SAML2TrustedIdPSBOCustomImpl extends
		com.sap.security.saml2.cfg.custom.SAML2TrustedIdPCustomImpl {
	
	public SAML2TrustedIdPSBOCustomImpl(String name,
			X509Certificate signingCert, Collection<SAML2Endpoint> endpoints)
			throws IllegalArgumentException {
		super(name, signingCert, endpoints);
		// TODO Auto-generated constructor stub
	}

	public SAML2TrustedIdPSBOCustomImpl(String name,
			X509Certificate signingCert, String ssoLocationUrl,
			String sloLocationUrl, String arsLocationUrl)
			throws IllegalArgumentException {
		super(name, signingCert, ssoLocationUrl, sloLocationUrl, arsLocationUrl);
		// TODO Auto-generated constructor stub
	}

	public SAML2TrustedIdPSBOCustomImpl(String name,
			X509Certificate signingCert, String ssoLocationUrl)
			throws IllegalArgumentException {
		super(name, signingCert, ssoLocationUrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SignatureOption isToSignSingleLogoutMessages() {
		// TODO Auto-generated method stub
		return SignatureOption.NEVER;
	}
}
