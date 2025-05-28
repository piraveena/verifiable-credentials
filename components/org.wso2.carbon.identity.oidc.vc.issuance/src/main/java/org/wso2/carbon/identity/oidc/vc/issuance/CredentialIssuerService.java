package org.wso2.carbon.identity.oidc.vc.issuance;

import org.wso2.carbon.identity.oidc.vc.issuance.internal.OIDC4VCIServiceHolder;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Service to provide OpenID Credential Issuer metadata.
 */
public class CredentialIssuerService {

    public  Map<String, Object> getMetadata(HttpServletRequest request, String tenantDomain) throws CredentialIssuerMetadataException {

        if (tenantDomain == null || tenantDomain.isEmpty()) {
            throw new CredentialIssuerMetadataException("Tenant domain cannot be null or empty.");
        }
        CredentialIssuerMetadataProvider metadataProvider = OIDC4VCIServiceHolder.getInstance()
                .getCredentialIssuerMetadataProvider();

        Map<String, Object> metadata = metadataProvider.getCredentialIssuerMetadataResponse(request, tenantDomain);
        return metadata;

    }
}
