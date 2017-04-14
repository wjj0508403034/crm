package com.huoyun.saml2.configuration;

import java.security.cert.X509Certificate;

public class SAML2Configuration {
	private String localSPName;

	private String defaultIdPName;

	public String localACSEndpoint;

	public boolean ssoEnabled;

	private X509Certificate idpCert;

	private String ssoEndpoint;

	private String sloEndpoint;

	private String homePage;

	public String getLocalSPName() {
		return localSPName;
	}

	public void setLocalSPName(String localSPName) {
		this.localSPName = localSPName;
	}

	public String getDefaultIdPName() {
		return defaultIdPName;
	}

	public void setDefaultIdPName(String defaultIdPName) {
		this.defaultIdPName = defaultIdPName;
	}

	public String getLocalACSEndpoint() {
		return localACSEndpoint;
	}

	public void setLocalACSEndpoint(String localACSEndpoint) {
		this.localACSEndpoint = localACSEndpoint;
	}

	public boolean isSsoEnabled() {
		return ssoEnabled;
	}

	public void setSsoEnabled(boolean ssoEnabled) {
		this.ssoEnabled = ssoEnabled;
	}

	public X509Certificate getIdpCert() {
		return idpCert;
	}

	public void setIdpCert(X509Certificate idpCert) {
		this.idpCert = idpCert;
	}

	public String getSsoEndpoint() {
		return ssoEndpoint;
	}

	public void setSsoEndpoint(String ssoEndpoint) {
		this.ssoEndpoint = ssoEndpoint;
	}

	public String getSloEndpoint() {
		return sloEndpoint;
	}

	public void setSloEndpoint(String sloEndpoint) {
		this.sloEndpoint = sloEndpoint;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
}
