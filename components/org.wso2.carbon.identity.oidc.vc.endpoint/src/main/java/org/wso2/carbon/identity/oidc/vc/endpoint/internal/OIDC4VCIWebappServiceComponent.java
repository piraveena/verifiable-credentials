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

import org.eclipse.equinox.http.helper.ContextPathServletAdaptor;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.identity.oidc.vc.endpoint.servlet.MetadataEndpointServlet;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;

import javax.servlet.Servlet;

/**
 * OSGi Service Component for OIDC4VCI metadata endpoint.
 * This component registers the metadata endpoint servlet and manages dependencies.
 */
@Component(name = "org.wso2.carbon.identity.vc.endpoint.component" + "", immediate = true, service =
        OIDC4VCIWebappServiceComponent.class)
public class OIDC4VCIWebappServiceComponent {


    @Activate
    protected void activate(ComponentContext componentContext) {

        try {

            HttpService httpService = DataHolder.getInstance().getHttpService();

            Servlet webFingerServlet = new ContextPathServletAdaptor(new MetadataEndpointServlet(),
                    "/.well-known/openid-credential-issuer");
            try {
                httpService.registerServlet("/.well-known/openid-credential-issuer", webFingerServlet, null, null);
            } catch (Exception e) {
                String errMsg = "Error when registering Web Finger Servlet via the HttpService.";
                throw new RuntimeException(errMsg, e);
            }

        } catch (Exception e) {
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {

    }

    @Reference(name = "osgi.http.service", service = HttpService.class, cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC, unbind = "unsetHttpService")
    protected void setHttpService(HttpService httpService) {

        DataHolder.getInstance().setHttpService(httpService);
    }

    protected void unsetHttpService(HttpService httpService) {

        DataHolder.getInstance().setHttpService(null);
    }

    @Reference(
            name = "identity.oidcvci.provider",
            service = CredentialIssuerMetadataProvider.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetCredentialIssuerMetadataProvider"
    )
    protected void setCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        DataHolder.getInstance().setCredentialIssuerMetadataProvider(credentialIssuerMetadataProvider);
    }

    /**
     * Unsets the Credential Issuer MetadataProvider.
     *
     * @param credentialIssuerMetadataProvider   CredentialIssuerMetadataProvider
     */
    protected void unsetCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        DataHolder.getInstance().setCredentialIssuerMetadataProvider(null);
    }
}
