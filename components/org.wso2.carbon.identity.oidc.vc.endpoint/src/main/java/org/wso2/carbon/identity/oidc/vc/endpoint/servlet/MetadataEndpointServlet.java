package org.wso2.carbon.identity.oidc.vc.endpoint.servlet;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oidc.vc.endpoint.internal.DataHolder;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataException;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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

    private static final Log LOG = LogFactory.getLog(MetadataEndpointServlet.class);
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String TENANT_DOMAIN_PARAM = "tenant";
    private static final String DEFAULT_TENANT = "carbon.super";
    private CredentialIssuerMetadataProvider metadataProvider;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            metadataProvider = DataHolder.getInstance().getCredentialIssuerMetadataProvider();
            LOG.info("OIDC4VCI Metadata Endpoint Servlet initialized successfully");
        } catch (CredentialIssuerMetadataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        LOG.debug("Received GET request for credential issuer metadata");
        try {
            Map<String, Object> metadata = metadataProvider.getCredentialIssuerMetadataResponse(request, "carbon" +
                        ".super");

            response.setContentType(CONTENT_TYPE_JSON);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);

            String responseString = new Gson().toJson(metadata);
            PrintWriter out = response.getWriter();
            out.print(responseString);

        } catch (CredentialIssuerMetadataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

    }

//    private String getTenantDomain() {
//
//        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
//        if (StringUtils.isEmpty(tenantDomain)) {
//            tenantDomain = IdentityTenantUtil.getTenantDomainFromContext();
//        }
//        return tenantDomain;
//    }

    /**
     * Extract tenant domain from request.
     * Priority: query parameter > subdomain > default
     */

}
