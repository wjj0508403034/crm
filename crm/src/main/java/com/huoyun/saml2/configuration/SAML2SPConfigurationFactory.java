package com.huoyun.saml2.configuration;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.huoyun.saml2.SAMLProperties;
import com.sap.security.saml2.cfg.custom.SAML2LocalSPCustomImpl;
import com.sap.security.saml2.cfg.enums.SAML2Binding;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;

public class SAML2SPConfigurationFactory {
	private SAML2SPConfigurationLoader configurationLoader;
	private volatile SAML2Configuration saml2Configuration = new SAML2Configuration();
	private volatile long lastCachedTimepstamp = 0;
	private volatile long cachedTimeInmilliseconds = 600000;
	private SAMLProperties samlProperties;

	public SAML2SPConfigurationFactory(SAMLProperties samlProperties) throws SAML2ConfigurationException {
		this.configurationLoader = new SAML2SPConfigurationLoader(samlProperties);
		configurationLoader.loadSPConfigurationFromFile(saml2Configuration);
	}

	public SAML2SPConfigurationCustom createSAML2SPConfiguration() {
		refreshSAML2Configuration();
		SAML2TrustedIdPSBOCustomImpl trustedIdP = new SAML2TrustedIdPSBOCustomImpl(
				saml2Configuration.getDefaultIdPName(), saml2Configuration.getIdpCert(),
				saml2Configuration.getSsoEndpoint(), saml2Configuration.getSloEndpoint(), null);
		trustedIdP.setResponseRequirementBinding(SAML2Binding.HTTP_POST_BINDING);
		trustedIdP.setSignAuthnRequests(false);
		trustedIdP.setSignSingleLogoutMessages(false);
		SAML2LocalSPCustomImpl localSP = new SAML2LocalSPCustomImpl(saml2Configuration.getLocalSPName(), null, null);

		return new SAML2SPConfigurationCustom(trustedIdP, localSP);
	}

	public SAML2Configuration getSAML2Configuration() {
		refreshSAML2Configuration();
		return saml2Configuration;
	}

	private void refreshSAML2Configuration() {
		Date now = new Date();
		if (saml2Configuration.isSsoEnabled() && now.getTime() - lastCachedTimepstamp > cachedTimeInmilliseconds) {
			synchronized (this) {
				if (now.getTime() - lastCachedTimepstamp > cachedTimeInmilliseconds) {
					configurationLoader.loadConfigurationsFromSLD(saml2Configuration);
					lastCachedTimepstamp = now.getTime();
				}
			}
		}
	}

	public String getAcsUrl(HttpServletRequest httpRequest) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://crm.fccfc.com").append(saml2Configuration.localACSEndpoint);
		return sb.toString();
//		StringBuilder sb = new StringBuilder();
//		sb.append(httpRequest.getScheme()).append("://").append(httpRequest.getServerName());
//		if ((httpRequest.getScheme().equals("https") && httpRequest.getServerPort() != 443)
//				|| (httpRequest.getScheme().equals("http") && httpRequest.getServerPort() != 80)) {
//			sb.append(":").append(httpRequest.getServerPort());
//		}
//		sb.append(httpRequest.getContextPath()).append(saml2Configuration.localACSEndpoint);
//		return sb.toString();
	}
}
