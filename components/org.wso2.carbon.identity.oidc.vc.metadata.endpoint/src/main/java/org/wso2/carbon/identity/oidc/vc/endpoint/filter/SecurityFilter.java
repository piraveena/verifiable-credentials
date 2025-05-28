package org.wso2.carbon.identity.oidc.vc.endpoint.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security filter for OIDC4VCI endpoints.
 * Handles authentication and authorization for protected resources.
 */
public class SecurityFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(SecurityFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("SecurityFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Skip OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        LOG.debug("Processing security filter for: " + httpRequest.getRequestURI());

        try {
            // Validate authorization header
            if (!isValidAuthorization(httpRequest)) {
                LOG.debug("Invalid or missing authorization");
                sendUnauthorizedResponse(httpResponse);
                return;
            }

            // Validate request content type for POST requests
            if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
                String contentType = httpRequest.getContentType();
                if (contentType == null || !contentType.toLowerCase().contains("application/json")) {
                    LOG.debug("Invalid content type: " + contentType);
                    sendBadRequestResponse(httpResponse, "Content-Type must be application/json");
                    return;
                }
            }

            // Proceed with the request
            chain.doFilter(request, response);

        } catch (Exception e) {
            LOG.error("Error in security filter", e);
            sendInternalErrorResponse(httpResponse);
        }
    }

    @Override
    public void destroy() {
        LOG.info("SecurityFilter destroyed");
    }

    /**
     * Validate authorization header
     */
    private boolean isValidAuthorization(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return false;
        }
        
        String accessToken = authHeader.substring(BEARER_PREFIX.length());
        
        // TODO: Implement proper token validation
        // For now, just check if token is not empty and has minimum length
        return accessToken != null && accessToken.trim().length() >= 10;
    }

    /**
     * Send unauthorized response
     */
    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String errorResponse = "{\"error\":\"invalid_token\",\"error_description\":\"Missing or invalid access token\"}";
        response.getWriter().write(errorResponse);
        response.getWriter().flush();
    }

    /**
     * Send bad request response
     */
    private void sendBadRequestResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String errorResponse = "{\"error\":\"invalid_request\",\"error_description\":\"" + message + "\"}";
        response.getWriter().write(errorResponse);
        response.getWriter().flush();
    }

    /**
     * Send internal server error response
     */
    private void sendInternalErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String errorResponse = "{\"error\":\"server_error\",\"error_description\":\"Internal server error\"}";
        response.getWriter().write(errorResponse);
        response.getWriter().flush();
    }
}
