package org.wso2.carbon.identity.oidc.vc.issuance.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataException;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class DefaultCredentialIssuerMetadataProvider implements CredentialIssuerMetadataProvider {

    private static final Log LOG = LogFactory.getLog(DefaultCredentialIssuerMetadataProvider.class);
    private String credentialIssuer;
    private String credentialEndpoint;
    private String[] authorizationServers;
    private JSONObject credentialConfigurationsSupported;

    private static DefaultCredentialIssuerMetadataProvider defaultCredentialIssuerMetadataProvider =
            new DefaultCredentialIssuerMetadataProvider();

    public static DefaultCredentialIssuerMetadataProvider getInstance()
            throws CredentialIssuerMetadataException {
        return defaultCredentialIssuerMetadataProvider;
    }

    private void setCredentialEndpoint(String tenantDomain) {
        this.credentialEndpoint = "https://test.example.com/" + tenantDomain + "/credential_endpoint";
    }

    public void setCredentialIssuer(String tenantDomain) throws CredentialIssuerMetadataException {
        this.credentialIssuer = "https://test.example.com/" + tenantDomain;
    }

    @Override
    public Map<String, Object> getCredentialIssuerMetadataResponse(HttpServletRequest request, String tenantDomain)
            throws CredentialIssuerMetadataException {

        if (tenantDomain == null || tenantDomain.isEmpty()) {
            throw new CredentialIssuerMetadataException("Tenant domain cannot be null or empty.");
        }

        try {
            buildMetaData(tenantDomain);
        } catch (CredentialIssuerMetadataException e) {
            LOG.error("Error while building metadata for tenant: " + tenantDomain, e);
            throw e;
        }

        return getConfigMap();
    }

    public void setAuthorizationServers(String tenantDomain) throws CredentialIssuerMetadataException {
        this.authorizationServers = new String[]{"https://test.example.com/" + tenantDomain};
    }

    public void setCredentialConfigurationsSupported(String credentialConfigurationType) {
        this.credentialConfigurationsSupported = new JSONObject();
        credentialConfigurationsSupported.put("credential_configuration_1", "University Degree Certificate");
    }

    private Map<String, Object> getConfigMap() {
        Map<String, Object> config = new HashMap<>();
        config.put("credential_issuer", credentialIssuer != null ? credentialIssuer : "default-issuer");
        config.put("credential_endpoint", credentialEndpoint != null ? credentialEndpoint : "default-endpoint");
        config.put("authorization_servers", authorizationServers != null ? authorizationServers : new String[]{"default-server"});
        config.put("credential_configurations_supported", credentialConfigurationsSupported != null ? credentialConfigurationsSupported : new JSONObject());
        return config;
    }

    private void buildMetaData(String tenantDomain) throws CredentialIssuerMetadataException {
        LOG.debug("Initializing DefaultCredentialIssuerMetadataProvider for OpenID connect for Credential Issuance");

        setCredentialIssuer(tenantDomain);
        setCredentialEndpoint(tenantDomain);
        setAuthorizationServers(tenantDomain);
        setCredentialConfigurationsSupported("credential_configuration_1");
    }
}
