package org.wso2.carbon.identity.oidc.vc.endpoint.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.identity.oidc.vc.endpoint.internal.OIDC4VCIWebappServiceHolder;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Servlet for handling OIDC4VCI metadata endpoint requests.
 * This endpoint provides the credential issuer metadata as per OpenID4VCI specification.
 * Deployed as an OSGi service using HTTP Whiteboard pattern.
 */
public class MetadataEndpointServlet extends HttpServlet {

    private static final Log LOG = LogFactory.getLog(MetadataEndpointServlet.class);
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String TENANT_DOMAIN_PARAM = "tenant";
    private static final String DEFAULT_TENANT = "carbon.super";

    @Override
    public void init() throws ServletException {
        super.init();
        LOG.info("OIDC4VCI Metadata Endpoint Servlet initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        LOG.debug("Received GET request for credential issuer metadata");
        
        String tenantDomain = extractTenantDomain(request);
        
        try {
            // Get metadata provider from OSGi service holder
            CredentialIssuerMetadataProvider metadataProvider = 
                OIDC4VCIWebappServiceHolder.getInstance().getCredentialIssuerMetadataProvider();
            
            if (metadataProvider == null) {
                LOG.error("Metadata provider not available");
                handleError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, 
                           "service_unavailable", "Metadata provider not available");
                return;
            }
            
            Map<String, Object> metadata = metadataProvider.getCredentialIssuerMetadataResponse(request, tenantDomain);
            
            response.setContentType(CONTENT_TYPE_JSON);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            
            // Add CORS headers
            addCORSHeaders(response);
            
            // Convert metadata to JSON and write response
            JSONObject jsonResponse = new JSONObject(metadata);
            
            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonResponse.toString(2)); // Pretty print with 2 spaces
                writer.flush();
            }
            
            LOG.debug("Successfully returned credential issuer metadata for tenant: " + tenantDomain);
            
        } catch (CredentialIssuerMetadataException e) {
            LOG.error("Error retrieving credential issuer metadata for tenant: " + tenantDomain, e);
            handleError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                       "Internal server error", e.getMessage());
        } catch (Exception e) {
            LOG.error("Unexpected error in metadata endpoint", e);
            handleError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                       "Internal server error", "An unexpected error occurred");
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
     * Extract tenant domain from request.
     * Priority: query parameter > subdomain > default
     */
    private String extractTenantDomain(HttpServletRequest request) {
        // Check query parameter first
        String tenantDomain = request.getParameter(TENANT_DOMAIN_PARAM);
        
        if (tenantDomain == null || tenantDomain.trim().isEmpty()) {
            // Try to extract from subdomain
            String serverName = request.getServerName();
            if (serverName != null && serverName.contains(".")) {
                String[] parts = serverName.split("\\.");
                if (parts.length > 2) {
                    tenantDomain = parts[0];
                }
            }
        }
        
        return (tenantDomain != null && !tenantDomain.trim().isEmpty()) ? 
               tenantDomain.trim() : DEFAULT_TENANT;
    }

    /**
     * Add CORS headers to response
     */
    private void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
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
