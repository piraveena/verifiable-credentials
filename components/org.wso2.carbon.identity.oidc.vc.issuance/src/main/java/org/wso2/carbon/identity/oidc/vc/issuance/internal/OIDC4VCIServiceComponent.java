package org.wso2.carbon.identity.oidc.vc.issuance.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;
import org.wso2.carbon.identity.oidc.vc.issuance.impl.DefaultCredentialIssuerMetadataProvider;

@Component(
        name = "oidc4vci.service.component",
        immediate = true
)
public class OIDC4VCIServiceComponent {

    private static final Log log = LogFactory.getLog(OIDC4VCIServiceComponent.class);
    private static BundleContext bundleContext = null;

    public static BundleContext getBundleContext() {
        return bundleContext;
    }

    protected void activate(ComponentContext context) {
        try {
            bundleContext = context.getBundleContext();
            bundleContext.registerService(CredentialIssuerMetadataProvider.class.getName(), DefaultCredentialIssuerMetadataProvider.getInstance(), null);
            // exposing server configuration as a service
            if (log.isDebugEnabled()) {
                log.debug("Identity OIDC4VCIServiceComponent bundle is activated");
            }
        } catch (Throwable e) {
            log.error("Error while activating OIDC4VCIServiceComponent", e);
        }
    }


    @Reference(
            name = "identity.oidcvci.provider",
            service = CredentialIssuerMetadataProvider.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetCredentialIssuerMetadataProvider"
    )
    protected void setCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Adding the CredentialIssuerMetadataProvider Service : %s", credentialIssuerMetadataProvider.getClass().getName()));
        }
        OIDC4VCIServiceHolder.getInstance().setCredentialIssuerMetadataProvider(credentialIssuerMetadataProvider);
    }

    /**
     * Unsets the Credential Issuer MetadataProvider.
     *
     * @param credentialIssuerMetadataProvider   CredentialIssuerMetadataProvider
     */
    protected void unsetCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Removing the Credential Issuer MetadataProvider Service : %s", credentialIssuerMetadataProvider.getClass().getName()));
        }
        OIDC4VCIServiceHolder.getInstance().setCredentialIssuerMetadataProvider(null);
    }

}
