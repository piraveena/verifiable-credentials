package org.wso2.carbon.identity.oidc.vc.issuance.internal;

import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataException;
import org.wso2.carbon.identity.oidc.vc.issuance.impl.DefaultCredentialIssuerMetadataProvider;

public class OIDC4VCIServiceHolder {

    private static OIDC4VCIServiceHolder instance = new OIDC4VCIServiceHolder();

    private CredentialIssuerMetadataProvider credentialIssuerMetadataProvider;


    private OIDC4VCIServiceHolder() {
        // Private constructor to prevent instantiation
    }

    public static OIDC4VCIServiceHolder getInstance() {

        return instance;
    }

    public void setCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        this.credentialIssuerMetadataProvider = credentialIssuerMetadataProvider;
    }

    public CredentialIssuerMetadataProvider getCredentialIssuerMetadataProvider() throws CredentialIssuerMetadataException {

        if (credentialIssuerMetadataProvider == null) {
            credentialIssuerMetadataProvider = DefaultCredentialIssuerMetadataProvider.getInstance();
        }
        return credentialIssuerMetadataProvider;
    }
}
