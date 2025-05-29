/*
 * Copyright (c) 2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oidc.vc.endpoint.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.equinox.http.helper.ContextPathServletAdaptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.identity.oidc.vc.endpoint.endpoint.MetadataEndpointServlet;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;

import javax.servlet.Servlet;
import java.util.Hashtable;

/**
 * OSGi Service Component for OIDC4VCI metadata endpoint.
 * This component registers the metadata endpoint servlet and manages dependencies.
 */
@Component(
    name = "org.wso2.carbon.identity.oidc4vci.webapp.component",
    immediate = true,
    service = OIDC4VCIWebappServiceComponent.class
)
public class OIDC4VCIWebappServiceComponent {

    private static final Log LOG = LogFactory.getLog(OIDC4VCIWebappServiceComponent.class);
    
    private ServiceRegistration<Servlet> metadataServletRegistration;
    private ServiceRegistration<Servlet> credentialServletRegistration;

    @Activate
    protected void activate(ComponentContext componentContext) {
        try {

            LOG.debug("OIDC4VCIWebappServiceComponent is going to get activated.");

            HttpService httpService = OIDC4VCIWebappServiceHolder.getInstance().getHttpService();
            Servlet metadataEndpointServlet = new ContextPathServletAdaptor(new MetadataEndpointServlet(),
                        "/.well-known/openid-credential-issuer");

            httpService.registerServlet("/.well-known/openid-credential-issuer", metadataEndpointServlet, null, null);
            LOG.info("OIDC4VCI Webapp Service Component activated successfully");
            
        } catch (Exception e) {
            LOG.error("Error activating OIDC4VCI Webapp Service Component", e);
            throw new RuntimeException("Failed to activate OIDC4VCI Webapp Service Component", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {
        try {
            // Unregister services
            if (metadataServletRegistration != null) {
                metadataServletRegistration.unregister();
                metadataServletRegistration = null;
                LOG.debug("Unregistered metadata servlet");
            }
            
            if (credentialServletRegistration != null) {
                credentialServletRegistration.unregister();
                credentialServletRegistration = null;
                LOG.debug("Unregistered credential servlet");
            }
            
            LOG.info("OIDC4VCI Webapp Service Component deactivated successfully");
            
        } catch (Exception e) {
            LOG.error("Error deactivating OIDC4VCI Webapp Service Component", e);
        }
    }

    /**
     * Set the CredentialIssuerMetadataProvider service reference
     */
    @Reference(
        name = "identity.oidc4vci.metadata.provider",
        service = CredentialIssuerMetadataProvider.class,
        cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.DYNAMIC,
        unbind = "unsetCredentialIssuerMetadataProvider"
    )
    protected void setCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider metadataProvider) {
        LOG.debug("Setting CredentialIssuerMetadataProvider: " + metadataProvider.getClass().getName());
        OIDC4VCIWebappServiceHolder.getInstance().setCredentialIssuerMetadataProvider(metadataProvider);
    }

    /**
     * Unset the CredentialIssuerMetadataProvider service reference
     */
    protected void unsetCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider metadataProvider) {
        LOG.debug("Unsetting CredentialIssuerMetadataProvider: " + metadataProvider.getClass().getName());
        OIDC4VCIWebappServiceHolder.getInstance().setCredentialIssuerMetadataProvider(null);
    }

    @Reference(
            name = "osgi.http.service",
            service = HttpService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetHttpService"
    )
    protected void setHttpService(HttpService httpService) {

        LOG.debug("HTTP Service is set in Trusted App mgt bundle");
        OIDC4VCIWebappServiceHolder.getInstance().setHttpService(httpService);
    }

    protected void unsetHttpService(HttpService httpService) {


        LOG.debug("HTTP Service is unset in the Trusted App mgt bundle");
        OIDC4VCIWebappServiceHolder.getInstance().setHttpService(null);

    }
}
