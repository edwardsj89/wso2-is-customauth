package za.co.brightrock.wso2.identity.auth.customauthenticator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.common.exception.InvalidOAuthClientException;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.bean.OAuthClientAuthnContext;
import org.wso2.carbon.identity.oauth2.client.authentication.AbstractOAuthClientAuthenticator;
import org.wso2.carbon.identity.oauth2.client.authentication.OAuthClientAuthnException;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Sample client authenticator which will authenticate client request using
 * client_id and cleint_secret which are available as two separate HTTP headers.
 */
public class CustomOAuthClientAuthenticator extends AbstractOAuthClientAuthenticator {
    private static final Log log = LogFactory.getLog(CustomOAuthClientAuthenticator.class);

    /**
     * Authenticates oauth client based on client_id and client_secret headers.
     *
     * @param httpServletRequest      Incoming HttpServlet request.
     * @param bodyParams              body content map.
     * @param oAuthClientAuthnContext
     * @return True if successfully authenticates. False if authentication failes
     * @throws OAuthClientAuthnException
     */
    public boolean authenticateClient(HttpServletRequest httpServletRequest, Map<String, List> bodyParams,
            OAuthClientAuthnContext oAuthClientAuthnContext) throws OAuthClientAuthnException {

        String clientId = httpServletRequest.getHeader("client_id");
        String clientSecret = httpServletRequest.getHeader("client_secret");

        log.info("authenticateClient - client_id: " + clientId + ", client_secret: " + clientSecret);
        try {
            return OAuth2Util.authenticateClient(clientId, clientSecret);
        } catch (IdentityOAuthAdminException | InvalidOAuthClientException | IdentityOAuth2Exception e) {
            throw new OAuthClientAuthnException("Error while authenticating client", "INVALID_CLIENT", e);
        }
    }

    /**
     * Returns whether this request can be authenticated by this client
     * authenticator.
     *
     * @param httpServletRequest      Incoming HttpServletRequest.
     * @param bodyParams              Content of the body.
     * @param oAuthClientAuthnContext OAuth Client authentication context.
     * @return True if the client can be authenticated and false else.
     */
    public boolean canAuthenticate(HttpServletRequest httpServletRequest, Map<String, List> bodyParams,
            OAuthClientAuthnContext oAuthClientAuthnContext) {

        if (httpServletRequest.getHeader("client_id") != null
                && httpServletRequest.getHeader("client_secret") != null) {
            log.info("HttpServletRequest  - client_id: " + httpServletRequest.getHeader("client_id")
                    + ", client_secret: " + httpServletRequest.getHeader("client_secret"));
            return true;
        }
        return false;
    }

    /**
     * Extracts client id from incoming request and sends out.
     *
     * @param httpServletRequest      Incoming HttpServletRequest.
     * @param bodyParams              Content of the request body.
     * @param oAuthClientAuthnContext OAuth client authentication context
     * @return Client Id.
     * @throws OAuthClientAuthnException OAuthClientAuthenticationException.
     */
    public String getClientId(HttpServletRequest httpServletRequest, Map<String, List> bodyParams,
            OAuthClientAuthnContext oAuthClientAuthnContext) throws OAuthClientAuthnException {

        log.info("getClientId - client_id: " + httpServletRequest.getHeader("client_id"));
        return httpServletRequest.getHeader("client_id");
    }

    /**
     * Returns the execution order of this sample authenticator.
     *
     * @return
     */
    @Override
    public int getPriority() {

        log.info("getPriority");
        return 899;
    }

    /**
     * Returns the name of this client authenticator.
     *
     * @return
     */
    @Override
    public String getName() {

        log.info("getName");
        return CustomOAuthClientAuthenticator.class.getName();
    }
}