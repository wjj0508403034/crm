package com.huoyun.saml2.configuration;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.saml2.SAMLProperties;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;

public class SAML2SPConfigurationLoader {
	private static Logger logger = LoggerFactory
			.getLogger(SAML2SPConfigurationLoader.class);

	private SAMLProperties samlProperties;

	public SAML2SPConfigurationLoader(SAMLProperties samlProperties) {
		this.samlProperties = samlProperties;
	}

	public SAML2Configuration loadSPConfigurationFromFile(
			SAML2Configuration configuration)
			throws SAML2ConfigurationException {

		configuration.setSsoEnabled(true);

		if (StringUtils.isEmpty(this.samlProperties.getPublicKey())) {
			throw new RuntimeException("IDPCertificate couldn't be found");
		}

		byte[] certRaw = Base64
				.decodeBase64(this.samlProperties.getPublicKey());
		try {
			CertificateFactory certFactory;
			certFactory = CertificateFactory.getInstance("X.509");
			X509Certificate idpCert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(certRaw));
			configuration.setIdpCert(idpCert);
		} catch (CertificateException e) {
			throw new SAML2ConfigurationException(e);
		}

		configuration.setDefaultIdPName(this.samlProperties.getIdpName());

		if (StringUtils.isBlank(this.samlProperties.getSpName()))
			throw new SAML2ConfigurationException(
					"Local SP name couldn't be found");

		configuration.setLocalSPName(this.samlProperties.getSpName());

		configuration.setLocalACSEndpoint(this.samlProperties.getAcs());
		configuration.setSsoEndpoint(this.samlProperties.getSso());
		configuration.setSloEndpoint(this.samlProperties.getSlo());

		logger.info(
				"SSO properties load from file, IDPName: '{}', LocalSPName: '{}', LocalACSEndpoint: '{}'",
				this.samlProperties.getIdpName(),
				this.samlProperties.getSpName(), this.samlProperties.getAcs());

		return configuration;

	}

	public SAML2Configuration loadConfigurationsFromSLD(
			SAML2Configuration configuration) {

		logger.info("SSO Endpoint Location: {}", this.samlProperties.getSso());
		logger.info("SLO Endpoint Location: {}", this.samlProperties.getSlo());

		configuration.setSsoEndpoint(this.samlProperties.getSso());
		configuration.setSloEndpoint(this.samlProperties.getSlo());
		configuration.setHomePage(this.samlProperties.getSpSuccessUrl());
		return configuration;
	}
}
