package org.wso2.carbon.identity.oidc.vc.issuance.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.oidc.vc.issuance.CredentialIssuerMetadataProvider;
import org.wso2.carbon.identity.oidc.vc.issuance.impl.DefaultCredentialIssuerMetadataProvider;

@Component(
        name = "oidc4vci.service.component",
        immediate = true,
        service = OIDC4VCIServiceComponent.class
)
public class OIDC4VCIServiceComponent {

    private static final Log LOG = LogFactory.getLog(OIDC4VCIServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {
        try {

            LOG.debug("OIDC4VCIServiceComponent is going to get activated.");
            context.getBundleContext().registerService(CredentialIssuerMetadataProvider.class.getName(),
                    DefaultCredentialIssuerMetadataProvider.getInstance(), null);
            // exposing server configuration as a service
            if (LOG.isDebugEnabled()) {
                LOG.debug("Identity OIDC4VCIServiceComponent bundle is activated");
            }
        } catch (Throwable e) {
            LOG.error("Error while activating OIDC4VCIServiceComponent", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {

        LOG.debug("OIDC4VCIServiceComponent component deactivated.");

    }


    @Reference(
            name = "identity.oidcvci.provider",
            service = CredentialIssuerMetadataProvider.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetCredentialIssuerMetadataProvider"
    )


    protected void setCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Adding the CredentialIssuerMetadataProvider Service : %s", credentialIssuerMetadataProvider.getClass().getName()));
        }
        OIDC4VCIServiceHolder.getInstance().setCredentialIssuerMetadataProvider(credentialIssuerMetadataProvider);
    }

    /**
     * Unsets the Credential Issuer MetadataProvider.
     *
     * @param credentialIssuerMetadataProvider   CredentialIssuerMetadataProvider
     */
    protected void unsetCredentialIssuerMetadataProvider(CredentialIssuerMetadataProvider credentialIssuerMetadataProvider) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Removing the Credential Issuer MetadataProvider Service : %s", credentialIssuerMetadataProvider.getClass().getName()));
        }
        OIDC4VCIServiceHolder.getInstance().setCredentialIssuerMetadataProvider(null);
    }

}
