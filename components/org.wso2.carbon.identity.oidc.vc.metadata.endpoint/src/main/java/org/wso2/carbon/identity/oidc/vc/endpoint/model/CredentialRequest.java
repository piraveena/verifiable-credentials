package org.wso2.carbon.identity.oidc.vc.endpoint.model;

import org.json.JSONObject;

/**
 * Model class representing a credential request in OIDC4VCI protocol.
 */
public class CredentialRequest {

    private String format;
    private JSONObject credentialDefinition;
    private JSONObject proof;

    public CredentialRequest() {
    }

    public CredentialRequest(String format, JSONObject credentialDefinition, JSONObject proof) {
        this.format = format;
        this.credentialDefinition = credentialDefinition;
        this.proof = proof;
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
     * Get the credential definition containing type and other metadata
     */
    public JSONObject getCredentialDefinition() {
        return credentialDefinition;
    }

    /**
     * Set the credential definition
     */
    public void setCredentialDefinition(JSONObject credentialDefinition) {
        this.credentialDefinition = credentialDefinition;
    }

    /**
     * Get the proof object containing cryptographic proof of possession
     */
    public JSONObject getProof() {
        return proof;
    }

    /**
     * Set the proof object
     */
    public void setProof(JSONObject proof) {
        this.proof = proof;
    }

    @Override
    public String toString() {
        return "CredentialRequest{" +
                "format='" + format + '\'' +
                ", credentialDefinition=" + credentialDefinition +
                ", proof=" + proof +
                '}';
    }
}
