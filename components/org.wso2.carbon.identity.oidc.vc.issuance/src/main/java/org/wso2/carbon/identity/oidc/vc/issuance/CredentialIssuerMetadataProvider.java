package org.wso2.carbon.identity.oidc.vc.issuance;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface CredentialIssuerMetadataProvider {

    /**
     * Returns the credential issuer metadata as a JSON object.
     *
     * @return JSONObject containing the metadata.
     */
    Map<String, Object> getCredentialIssuerMetadataResponse(HttpServletRequest request, String tenantDomain) throws
            CredentialIssuerMetadataException;

}
