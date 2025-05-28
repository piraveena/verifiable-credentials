package org.wso2.carbon.identity.oidc.vc.issuance;

/**
 * Exception class
 */
public class CredentialIssuerMetadataException extends Throwable {

    public CredentialIssuerMetadataException(String message, Throwable e) {
        super(message, e);
    }

    public CredentialIssuerMetadataException(String message) {
        super(message);
    }

}

