package za.co.brightrock.wso2.identity.auth.customauthenticator.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent;
import org.wso2.carbon.identity.oauth2.client.authentication.OAuthClientAuthenticator;

import za.co.brightrock.wso2.identity.auth.customauthenticator.CustomOAuthClientAuthenticator;

@Component(
        name = "za.co.brightrock.wso2.identity.auth.customauthenticator",
        immediate = true
)
public class CustomOAuthClientAuthenticatorComponent {

    private static final Log log = LogFactory.getLog(CustomOAuthClientAuthenticatorComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        try {
            CustomOAuthClientAuthenticator oAuthSampleClientAuthenticator = new CustomOAuthClientAuthenticator();
            context.getBundleContext().registerService(OAuthClientAuthenticator.class.getName(), oAuthSampleClientAuthenticator, null);
        } catch (Throwable e) {
            log.error("Error while activating OAuth2 sample client authenticator.", e);
        }
    }

    protected void unsetIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }

    @Reference(
            name = "identity.core.init.event.service",
            service = IdentityCoreInitializedEvent.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityCoreInitializedEventService"
    )
    protected void setIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }
}