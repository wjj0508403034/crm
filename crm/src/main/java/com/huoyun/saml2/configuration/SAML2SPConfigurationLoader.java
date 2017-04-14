package com.huoyun.saml2.configuration;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;

public class SAML2SPConfigurationLoader {
	private static Logger logger = LoggerFactory
			.getLogger(SAML2SPConfigurationLoader.class);

	public SAML2Configuration loadSPConfigurationFromFile(
			SAML2Configuration configuration)
			throws SAML2ConfigurationException {

		configuration.setSsoEnabled(true);

		String idpCertificate = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNoVENDQWU0Q0NRQys4eUNRdldLNUVUQU5CZ2txaGtpRzl3MEJBUXNGQURDQmhqRUxNQWtHQTFVRUJoTUMKUkVVeEREQUtCZ05WQkFnTUExaFlXREVNTUFvR0ExVUVCd3dEV0ZoWU1ROHdEUVlEVlFRS0RBWlRRVkF0VTBVeApEREFLQmdOVkJBc01BMU5OUlRFWU1CWUdBMVVFQXd3UEtpNXpiV1ZqTG5OaGNDNWpiM0p3TVNJd0lBWUpLb1pJCmh2Y05BUWtCRmhOMFpYTjBRSE5oY0dGdWVYZG9aWEpsTG1OdU1CNFhEVEUzTURJd016QXlOVE0wTTFvWERUSTMKTURJd01UQXlOVE0wTTFvd2dZWXhDekFKQmdOVkJBWVRBa1JGTVF3d0NnWURWUVFJREFOWVdGZ3hEREFLQmdOVgpCQWNNQTFoWVdERVBNQTBHQTFVRUNnd0dVMEZRTFZORk1Rd3dDZ1lEVlFRTERBTlRUVVV4R0RBV0JnTlZCQU1NCkR5b3VjMjFsWXk1ellYQXVZMjl5Y0RFaU1DQUdDU3FHU0liM0RRRUpBUllUZEdWemRFQnpZWEJoYm5sM2FHVnkKWlM1amJqQ0JuekFOQmdrcWhraUc5dzBCQVFFRkFBT0JqUUF3Z1lrQ2dZRUE3VDFRQTZkRC9RVmoxcEpPS2Z4MApxeVBZZWc0UjlkMVd6aFJaN09yejZLd0FHQVVNdVRDWEFHSlBDclJhMHdCUXl4ZytIOWV1K3R5K1pFdXpYOFpVCnA3N2pxRzhSTW5zeE14YkNwUkFXMXlZRitMVERzcVZzS2l1Y2ZqcXZkZ1dnZHZTM0hjWDREcHRDbVcwRmlKOWsKUEd0aFE0VmRuc2lhNFBYdE9tbmd3bzhDQXdFQUFUQU5CZ2txaGtpRzl3MEJBUXNGQUFPQmdRRHB2L0x3V2pmTAorOVJJWWtsRHMzSml1UlROanFFaHgvWkE5bXZXLzlwMTFsUTRHUWZJVktOeFZHL0JmNS9MQnkwa2czekxIaDlRCkloVDFwdnNvZVVPUHhUVGgwN0JqbmRPOU1wcnRKYVFFYzJjY29sYTErWTFLTmw5ampjZU1GMHgyYmRZZnExRmcKU0xGM2o4dnVWbDc5RUF5cmxsN21laGZEbUFHY2RFQUZxdz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K";
		if (null == idpCertificate || idpCertificate.trim().equals(""))
			throw new RuntimeException("IDPCertificate couldn't be found");

		byte[] certRaw = Base64.decodeBase64(idpCertificate);
		try {
			CertificateFactory certFactory;
			certFactory = CertificateFactory.getInstance("X.509");
			X509Certificate idpCert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(certRaw));
			configuration.setIdpCert(idpCert);
		} catch (CertificateException e) {
			throw new SAML2ConfigurationException(e);
		}

		String idpName = "localhost";

		configuration.setDefaultIdPName(idpName);
		String localSPName = "HuoYun";
		configuration.setLocalSPName(localSPName);

		if (StringUtils.isBlank(localSPName))
			throw new SAML2ConfigurationException(
					"Local SP name couldn't be found");

		configuration.setLocalACSEndpoint("/saml2/sp/acs");
		// configuration.setSsoEndpoint(ssoProperties.getProperty("SSOEndpoint"));
		// configuration.setSloEndpoint(ssoProperties.getProperty("SLOEndpoint"));

		logger.info(
				"SSO properties load from file, IDPName: '{}', LocalSPName: '{}', LocalACSEndpoint: '{}'",
				idpName, localSPName, "/saml2/sp/acs");

		return configuration;

	}

	public SAML2Configuration loadConfigurationsFromSLD(
			SAML2Configuration configuration) {

		if (configuration.isSsoEnabled()) {
			String sldExternalUrl = "http://localhost:4033/";
			String ssoEndpoint = sldExternalUrl + "saml2/idp/ssojson";
			String sloEndpoint = sldExternalUrl + "saml2/idp/slo";

			logger.info("SSO Endpoint Location: {}", ssoEndpoint);
			logger.info("SLO Endpoint Location: {}", sloEndpoint);

			configuration.setSsoEndpoint(ssoEndpoint);
			configuration.setSloEndpoint(sloEndpoint);

			String homePageUrl = "http://localhost:8080/index.html";

			configuration.setHomePage(homePageUrl);
		}
		return configuration;
	}
}
