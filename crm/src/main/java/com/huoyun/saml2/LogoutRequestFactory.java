package com.huoyun.saml2;

import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.sap.security.saml2.cfg.enums.EncryptionAlgorithm;
import com.sap.security.saml2.cfg.exceptions.SAML2ConfigurationException;
import com.sap.security.saml2.cfg.interfaces.read.SAML2LocalSP;
import com.sap.security.saml2.cfg.interfaces.read.SAML2TrustedIdP;
import com.sap.security.saml2.commons.SAML2Principal;
import com.sap.security.saml2.lib.common.SAML2DataFactory;
import com.sap.security.saml2.lib.common.SAML2ProtocolFactory;
import com.sap.security.saml2.lib.common.SAML2Utils;
import com.sap.security.saml2.lib.interfaces.assertions.SAML2EncryptedNameID;
import com.sap.security.saml2.lib.interfaces.assertions.SAML2NameID;
import com.sap.security.saml2.lib.interfaces.protocols.SAML2LogoutRequest;
import com.sap.security.saml2.sp.sso.LogoutRequestData;
import com.sap.tc.logging.Category;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;

public class LogoutRequestFactory {
	private static final Location LOCATION = Location.getLocation(LogoutRequestFactory.class);
	  private static final Category CATEGORY = Category.getCategory(Category.SYS_SECURITY, "Authentication");
	  
		static SAML2LogoutRequest createLogoutRequest(SAML2LocalSP localSP, SAML2TrustedIdP trustedIdP, SAML2Principal saml2Principal, String idpSLOUrl, LoginResult loginResult)
				throws LoginException {

			if (loginResult == null) {
				loginResult = new LoginResult();
			}

			// Get necessary data for logout from the SAML2Principal
			String nameId = saml2Principal.getNameId();
			String nameIdFormat = saml2Principal.getNameIdFormat();
			String nameIdSPNameQualifier = saml2Principal.getNameIdSPNameQualifier();
			String nameIdNameQualifier = saml2Principal.getNameIdNameQualifier();
			List<String> sessionIndexes = saml2Principal.getSessionIndexes();

			// Create Name ID
			SAML2NameID userNameId = SAML2DataFactory.getInstance().createSAML2NameID(nameId);
			userNameId.setFormat(nameIdFormat);
			userNameId.setSPNameQualifier(nameIdSPNameQualifier);
			userNameId.setNameQualifier(nameIdNameQualifier);
			// TODO uncomment, when the new classes are synchronized
			// loginResult.getAuditData().setNameID(userNameId);

			return createLogoutRequest(localSP, trustedIdP, userNameId, idpSLOUrl, loginResult, sessionIndexes);

		}
	  
	  private static PublicKey getTrustedIdPPublicKeyForEncryption(SAML2TrustedIdP trustedIdP, LoginResult loginResult) throws LoginException {
	    PublicKey publicKey = null;
	    try {
	      publicKey = trustedIdP.getPublicKeyForEncryption();
	      if (publicKey == null) {
	        SimpleLogger.log(Severity.ERROR, CATEGORY, LOCATION, "ASJ.saml20_sp.000114",
	            "Service Provider cannot read trusted Identity Provider [{0}] public key for encryption from configuration.",
	            new Object[] {trustedIdP.getName()});
	        loginResult.throwLoginFailedException("Could not read trusted Identity Provider public key for encryption.");
	      }
	    } catch (SAML2ConfigurationException e) {
	      LOCATION.traceThrowableT(Severity.ERROR, "Could not read trusted Identity Provider public key for encryption.", e);
	      SimpleLogger.log(Severity.ERROR, CATEGORY, LOCATION, "ASJ.saml20_sp.000114",
	          "Service Provider cannot read trusted Identity Provider [{0}] public key for encryption from configuration.",
	          new Object[] {trustedIdP.getName()});
	      loginResult.throwLoginFailedException("Could not read trusted Identity Provider public key for encryption.", e);
	    }
	    return publicKey;
	  }
	  
		static SAML2LogoutRequest createLogoutRequest(SAML2LocalSP localSP, SAML2TrustedIdP trustedIdP, LogoutRequestData data, String idpSLOUrl, LoginResult loginResult)
				throws LoginException {

			if (loginResult == null) {
				loginResult = new LoginResult();
			}

			// Get necessary data for logout from the SAML2Principal
			String nameId = data.getNameId();
			String nameIdFormat = data.getNameIdFormat();
			String nameIdSPNameQualifier = data.getNameIdSPNameQualifier();
			String nameIdNameQualifier = data.getNameIdNameQualifier();
	    String nameIdSPProvidedId = data.getNameIdSPProvidedId();
			List<String> sessionIndexes = data.getSessionIndexes();

			// Create Name ID
			SAML2NameID userNameId = SAML2DataFactory.getInstance().createSAML2NameID(nameId);
			userNameId.setFormat(nameIdFormat);
			userNameId.setSPNameQualifier(nameIdSPNameQualifier);
			userNameId.setNameQualifier(nameIdNameQualifier);
			if(nameIdSPProvidedId != null) {
			  userNameId.setSPProvidedID(nameIdSPProvidedId);
			}
			// TODO uncomment, when the new classes are synchronized
			// loginResult.getAuditData().setNameID(userNameId);

			return createLogoutRequest(localSP, trustedIdP, userNameId, idpSLOUrl, loginResult, sessionIndexes);

		}

		private static SAML2LogoutRequest createLogoutRequest(SAML2LocalSP localSP, SAML2TrustedIdP trustedIdP, SAML2NameID userNameId, String idpSLOUrl, LoginResult loginResult,
				List<String> sessionIndexes) throws LoginException {

			// Create the LogoutRequest and fill its fields
			SAML2LogoutRequest logoutRequest;
			try {
				Date now = Calendar.getInstance().getTime();
				String requestId = SAML2Utils.generateUUID();

				if (trustedIdP.isToEncryptSingleLogoutSubject()) {
					// Create LogoutRequest with encrypted Name ID
					EncryptionAlgorithm encryptAlgorithm = trustedIdP.getEncryptionAlgorithm();
					if (encryptAlgorithm == null) {
						SimpleLogger.log(Severity.ERROR, CATEGORY, LOCATION, "ASJ.saml20_sp.000116",
								"Service Provider cannot read trusted Identity Provider [{0}] algorithm for encryption from configuration.", new Object[] { trustedIdP.getName() });
						loginResult.throwLoginFailedException("Encryption algorithm cannot be read from trusted IdP configuration.");
					}

					PublicKey idpEncPublicKey = getTrustedIdPPublicKeyForEncryption(trustedIdP, loginResult);
					SAML2EncryptedNameID encNameId = userNameId.encrypt(idpEncPublicKey, encryptAlgorithm.getName());
					if (LOCATION.beDebug()) {
						LOCATION.debugT("LogoutRequest Name ID successfully encrypted using algorithm: {0}", encryptAlgorithm.getName());
					}
					// TODO uncomment, when the new classes are synchronized
					// loginResult.getAuditData().setSAML2NameIDEncrypted(true);

					logoutRequest = SAML2ProtocolFactory.getInstance().createLogoutRequest(requestId, now, encNameId);
				} else {
					// TODO uncomment, when the new classes are synchronized
					// loginResult.getAuditData().setSAML2NameIDEncrypted(false);

					// Create LogoutRequest with plain Name ID
					logoutRequest = SAML2ProtocolFactory.getInstance().createLogoutRequest(requestId, now, userNameId);
				}

				// Set LogoutRequest session indexes list
				logoutRequest.setSessionIndex(sessionIndexes);
				if (LOCATION.beDebug()) {
					LOCATION.debugT("SesionIndex set in the LogoutRequest: {0}", new Object[] { sessionIndexes });
				}

				// Set LogoutRequest issuer
				SAML2NameID issuerNameId = SAML2DataFactory.getInstance().createSAML2NameID(localSP.getName());
				logoutRequest.setIssuer(issuerNameId);
				if (LOCATION.beDebug()) {
					LOCATION.debugT("Issuer set in the LogoutRequest: {0}", new Object[] { localSP.getName() });
				}

				// Set LogoutRequest destination
				logoutRequest.setDestination(idpSLOUrl);
				if (LOCATION.beDebug()) {
					LOCATION.debugT("Destination set in the LogoutRequest: {0}", new Object[] { idpSLOUrl });
				}

			} catch (Exception e) {
				LOCATION.traceThrowableT(Severity.ERROR, "LogoutRequest could not be created.", e);
				SimpleLogger.log(Severity.ERROR, CATEGORY, LOCATION, "ASJ.saml20_sp.000005",
						"Service Provider could not perform global logout because LogoutRequest could not be created. Reason: {0}", new Object[] { e.getMessage() });
				LoginException le = new LoginException("Service Provider could not perform global logout because LogoutRequest could not be created.");
				le.initCause(e);
				throw le;
			}

			return logoutRequest;
		}
}
