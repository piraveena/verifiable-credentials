package org.wso2.carbon.identity.oidc.vc.endpoint.model;

/**
 * Model class representing a credential response in OIDC4VCI protocol.
 */
public class CredentialResponse {

    private String format;
    private Object credential;
    private String credentialNonce;
    private Integer credentialExpiresIn;

    public CredentialResponse() {
    }

    public CredentialResponse(String format, Object credential) {
        this.format = format;
        this.credential = credential;
    }

    /**
     * Get the credential format (e.g., "jwt_vc", "ldp_vc", "vc+sd-jwt")
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the credential format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Get the issued credential. Can be a String (for JWT) or JSONObject (for JSON-LD)
     */
    public Object getCredential() {
        return credential;
    }

    /**
     * Set the issued credential
     */
    public void setCredential(Object credential) {
        this.credential = credential;
    }

    /**
     * Get the credential nonce (c_nonce) for next request
     */
    public String getCredentialNonce() {
        return credentialNonce;
    }

    /**
     * Set the credential nonce
     */
    public void setCredentialNonce(String credentialNonce) {
        this.credentialNonce = credentialNonce;
    }

    /**
     * Get the credential nonce expiration time in seconds
     */
    public Integer getCredentialExpiresIn() {
        return credentialExpiresIn;
    }

    /**
     * Set the credential nonce expiration time
     */
    public void setCredentialExpiresIn(Integer credentialExpiresIn) {
        this.credentialExpiresIn = credentialExpiresIn;
    }

    @Override
    public String toString() {
        return "CredentialResponse{" +
                "format='" + format + '\'' +
                ", credential=" + credential +
                ", credentialNonce='" + credentialNonce + '\'' +
                ", credentialExpiresIn=" + credentialExpiresIn +
                '}';
    }
}
