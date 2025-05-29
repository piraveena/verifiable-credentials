package org.wso2.carbon.identity.oidc.vc.endpoint.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling OIDC4VCI metadata endpoint requests.
 * This endpoint provides the credential issuer metadata as per OpenID4VCI specification.
 * Deployed as an OSGi service using HTTP Whiteboard pattern.
 */
public class MetadataEndpointServlet extends HttpServlet {

//    private static final Log LOG = LogFactory.getLog(MetadataEndpointServlet.class);
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String TENANT_DOMAIN_PARAM = "tenant";
    private static final String DEFAULT_TENANT = "carbon.super";

    @Override
    public void init() throws ServletException {
        super.init();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        

    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

    }

    /**
     * Extract tenant domain from request.
     * Priority: query parameter > subdomain > default
     */

}
