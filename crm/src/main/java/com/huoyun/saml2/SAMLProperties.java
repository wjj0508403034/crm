package com.huoyun.saml2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(locations = "classpath:/META-INF/saml.properties")
@Configuration
public class SAMLProperties {

	private String publicKey;

	private String acs;

	private String spName;

	private String idpName;

	private String sso;

	private String slo;
	
	private String spSuccessUrl;

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAcs() {
		return acs;
	}

	public void setAcs(String acs) {
		this.acs = acs;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getIdpName() {
		return idpName;
	}

	public void setIdpName(String idpName) {
		this.idpName = idpName;
	}

	public String getSso() {
		return sso;
	}

	public void setSso(String sso) {
		this.sso = sso;
	}

	public String getSlo() {
		return slo;
	}

	public void setSlo(String slo) {
		this.slo = slo;
	}

	public String getSpSuccessUrl() {
		return spSuccessUrl;
	}

	public void setSpSuccessUrl(String spSuccessUrl) {
		this.spSuccessUrl = spSuccessUrl;
	}
}
