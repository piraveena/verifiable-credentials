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

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * OSGi Service Component for OIDC4VCI metadata endpoint.
 * This component registers the metadata endpoint servlet and manages dependencies.
 */
@Component(
    name = "org.wso2.carbon.identity.vc.endpoint.component" +
            "",
    immediate = true,
    service = OIDC4VCIWebappServiceComponent.class
)
public class OIDC4VCIWebappServiceComponent {


    @Activate
    protected void activate(ComponentContext componentContext) {
        try {

         } catch (Exception e) {
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {

    }

}
