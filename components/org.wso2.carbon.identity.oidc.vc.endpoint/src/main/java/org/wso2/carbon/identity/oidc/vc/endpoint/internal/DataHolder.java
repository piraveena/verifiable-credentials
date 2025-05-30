package org.wso2.carbon.identity.oidc.vc.endpoint.internal;/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import org.osgi.service.http.HttpService;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataException;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;
import org.wso2.carbon.identity.oidc.vc.issuance.impl.DefaultCredentialIssuerMetadataProvider;

/**
 * Push Servlet Data Holder.
 */
public class DataHolder {

    private static final DataHolder instance = new DataHolder();
    private CredentialIssuerMetadataProvider credentialIssuerMetadataProvider;

    private HttpService httpService;

    private DataHolder() {

    }

    public static DataHolder getInstance() {

        return instance;
    }

    public HttpService getHttpService() {

        return httpService;
    }

    public void setHttpService(HttpService httpService) {

        this.httpService = httpService;
    }

    public void setCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        this.credentialIssuerMetadataProvider = credentialIssuerMetadataProvider;
    }

    public CredentialIssuerMetadataProvider getCredentialIssuerMetadataProvider() throws CredentialIssuerMetadataException {

        if (credentialIssuerMetadataProvider == null) {
            credentialIssuerMetadataProvider = DefaultCredentialIssuerMetadataProvider.getInstance();
        }
        return credentialIssuerMetadataProvider;
    }
}
