package org.wso2.carbon.identity.oidc.vc.endpoint.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.identity.oidc.vc.endpoint.model.CredentialRequest;
import org.wso2.carbon.identity.oidc.vc.endpoint.model.CredentialResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for handling OIDC4VCI credential endpoint requests.
 * This endpoint issues verifiable credentials according to OpenID4VCI specification.
 * Deployed as an OSGi service using HTTP Whiteboard pattern.
 */
public class CredentialEndpointServlet extends HttpServlet {

    private static final Log LOG = LogFactory.getLog(CredentialEndpointServlet.class);
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void init() throws ServletException {
        super.init();
        LOG.info("OIDC4VCI Credential Endpoint Servlet initialized successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        LOG.debug("Received POST request for credential issuance");
        
        try {
            // Validate authorization
            if (!isAuthorized(request)) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED, 
                           "invalid_token", "Missing or invalid access token");
                return;
            }
            
            // Parse request body
            CredentialRequest credentialRequest = parseCredentialRequest(request);
            
            if (credentialRequest == null) {
                handleError(response, HttpServletResponse.SC_BAD_REQUEST, 
                           "invalid_request", "Invalid credential request format");
                return;
            }
            
            // Process credential request
            CredentialResponse credentialResponse = processCredentialRequest(credentialRequest);
            
            // Send response
            sendCredentialResponse(response, credentialResponse);
            
            LOG.debug("Successfully processed credential request");
            
        } catch (Exception e) {
            LOG.error("Error processing credential request", e);
            handleError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                       "server_error", "An error occurred while processing the credential request");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle preflight CORS requests
        addCORSHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Validate authorization header
     */
    private boolean isAuthorized(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            LOG.debug("Missing or invalid authorization header");
            return false;
        }
        
        String accessToken = authHeader.substring(BEARER_PREFIX.length());
        
        // TODO: Implement proper token validation with OAuth2 server
        // For now, just check if token is not empty
        return accessToken != null && !accessToken.trim().isEmpty();
    }

    /**
     * Parse credential request from request body
     */
    private CredentialRequest parseCredentialRequest(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        if (requestBody.length() == 0) {
            return null;
        }
        
        try {
            JSONObject json = new JSONObject(requestBody.toString());
            
            CredentialRequest credentialRequest = new CredentialRequest();
            credentialRequest.setFormat(json.optString("format", "jwt_vc"));
            credentialRequest.setCredentialDefinition(json.optJSONObject("credential_definition"));
            credentialRequest.setProof(json.optJSONObject("proof"));
            
            return credentialRequest;
            
        } catch (Exception e) {
            LOG.error("Error parsing credential request", e);
            return null;
        }
    }

    /**
     * Process the credential request and generate response
     */
    private CredentialResponse processCredentialRequest(CredentialRequest request) {
        CredentialResponse response = new CredentialResponse();
        
        // TODO: Implement actual credential generation logic
        // This is a placeholder implementation
        
        if ("jwt_vc".equals(request.getFormat())) {
            // Generate a mock JWT credential
            String mockCredential = generateMockJWTCredential(request);
            response.setCredential(mockCredential);
            response.setFormat("jwt_vc");
        } else {
            // For other formats, return a simple JSON credential
            Map<String, Object> credential = generateMockJSONCredential(request);
            response.setCredential(credential);
            response.setFormat(request.getFormat());
        }
        
        // Set additional response fields
        response.setCredentialNonce("credential_nonce_" + System.currentTimeMillis());
        response.setCredentialExpiresIn(3600); // 1 hour
        
        return response;
    }

    /**
     * Generate a mock JWT credential (placeholder)
     */
    private String generateMockJWTCredential(CredentialRequest request) {
        // This is a mock implementation
        // In a real implementation, you would generate a proper JWT credential
        return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuY29tIiwic3ViIjoiZGlkOmV4YW1wbGU6MTIzIiwiZXhwIjoxNjQwOTk1MjAwLCJpYXQiOjE2NDA5OTE2MDAsInZjIjp7IkBjb250ZXh0IjpbImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIl0sInR5cGUiOlsiVmVyaWZpYWJsZUNyZWRlbnRpYWwiLCJVbml2ZXJzaXR5RGVncmVlQ3JlZGVudGlhbCJdLCJjcmVkZW50aWFsU3ViamVjdCI6eyJpZCI6ImRpZDpleGFtcGxlOjEyMyIsImRlZ3JlZSI6eyJ0eXBlIjoiQmFjaGVsb3JEZWdyZWUiLCJuYW1lIjoiQmFjaGVsb3Igb2YgU2NpZW5jZSBhbmQgQXJ0cyJ9fX19.mock_signature";
    }

    /**
     * Generate a mock JSON credential (placeholder)
     */
    private Map<String, Object> generateMockJSONCredential(CredentialRequest request) {
        Map<String, Object> credential = new HashMap<>();
        credential.put("@context", new String[]{"https://www.w3.org/2018/credentials/v1"});
        credential.put("type", new String[]{"VerifiableCredential", "UniversityDegreeCredential"});
        credential.put("issuer", "https://example.com");
        
        Map<String, Object> credentialSubject = new HashMap<>();
        credentialSubject.put("id", "did:example:123");
        
        Map<String, Object> degree = new HashMap<>();
        degree.put("type", "BachelorDegree");
        degree.put("name", "Bachelor of Science and Arts");
        credentialSubject.put("degree", degree);
        
        credential.put("credentialSubject", credentialSubject);
        credential.put("issuanceDate", "2024-01-01T00:00:00Z");
        
        return credential;
    }

    /**
     * Send credential response
     */
    private void sendCredentialResponse(HttpServletResponse response, CredentialResponse credentialResponse) 
            throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        addCORSHeaders(response);
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("format", credentialResponse.getFormat());
        jsonResponse.put("credential", credentialResponse.getCredential());
        
        if (credentialResponse.getCredentialNonce() != null) {
            jsonResponse.put("c_nonce", credentialResponse.getCredentialNonce());
        }
        
        if (credentialResponse.getCredentialExpiresIn() != null) {
            jsonResponse.put("c_nonce_expires_in", credentialResponse.getCredentialExpiresIn());
        }
        
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse.toString(2));
            writer.flush();
        }
    }

    /**
     * Add CORS headers to response
     */
    private void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    /**
     * Handle error responses
     */
    private void handleError(HttpServletResponse response, int statusCode, 
                           String error, String errorDescription) throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        
        addCORSHeaders(response);
        
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", error);
        errorResponse.put("error_description", errorDescription);
        
        try (PrintWriter writer = response.getWriter()) {
            writer.write(errorResponse.toString(2));
            writer.flush();
        }
    }
}
