package com.huoyun.saml2.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.saml2.cfg.custom.SAML2LocalSPCustomImpl;
import com.sap.security.saml2.cfg.interfaces.SAML2SPConfiguration;
import com.sap.security.saml2.cfg.interfaces.read.SAML2LocalSP;
import com.sap.security.saml2.cfg.interfaces.read.SAML2TrustedIdP;

public class SAML2SPConfigurationCustom implements SAML2SPConfiguration{
	private static Logger logger = LoggerFactory.getLogger(SAML2SPConfigurationCustom.class);

    private final SAML2TrustedIdPSBOCustomImpl trustedIdP;
    private final SAML2LocalSPCustomImpl localSP;

    public SAML2SPConfigurationCustom(SAML2TrustedIdPSBOCustomImpl trustedIdP, SAML2LocalSPCustomImpl localSP) {
        this.trustedIdP = trustedIdP;
        this.localSP = localSP;
    }

    public SAML2LocalSP getLocalSP() {
        return localSP;
    }

    public SAML2TrustedIdP getTrustedIdP(String name) {
        if (name.equals(trustedIdP.getName()))
            return trustedIdP;
        logger.warn(
                "Can not find IDP name in saml response the same with SP configuration, response idp name: {}, sp configuration name: {}",
                name, trustedIdP.getName());
        return null;
    }

    public SAML2TrustedIdP getTrustedIdP(byte[] id) {
        return null;
    }
}
