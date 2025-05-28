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
 * CORS (Cross-Origin Resource Sharing) filter for OIDC4VCI endpoints.
 * Handles preflight requests and adds appropriate CORS headers.
 */
public class CORSFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(CORSFilter.class);
    
    // CORS configuration - in production, these should be configurable
    private static final String ALLOWED_ORIGINS = "*";
    private static final String ALLOWED_METHODS = "GET, POST, OPTIONS, PUT, DELETE";
    private static final String ALLOWED_HEADERS = "Content-Type, Authorization, X-Requested-With, Accept";
    private static final String EXPOSED_HEADERS = "Content-Type, Authorization";
    private static final String MAX_AGE = "3600";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("CORSFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        LOG.debug("Processing CORS filter for: " + httpRequest.getRequestURI());

        // Add CORS headers
        addCORSHeaders(httpRequest, httpResponse);

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            LOG.debug("Handling CORS preflight request");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Continue with the request chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.info("CORSFilter destroyed");
    }

    /**
     * Add CORS headers to the response
     */
    private void addCORSHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        
        // In production, you should validate the origin against a whitelist
        if (origin != null && isAllowedOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else if ("*".equals(ALLOWED_ORIGINS)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        
        response.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
        response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        response.setHeader("Access-Control-Expose-Headers", EXPOSED_HEADERS);
        response.setHeader("Access-Control-Max-Age", MAX_AGE);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // Add security headers
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    /**
     * Check if the origin is allowed
     * In production, implement proper origin validation
     */
    private boolean isAllowedOrigin(String origin) {
        // For now, allow all origins for development
        // In production, check against a configured whitelist
        return true;
    }
}
