package ie.brianhenry.veintobrain;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import com.google.common.base.Optional;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, Boolean> {
    @Override
    public Optional<Boolean> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {
            return Optional.of(true);
        }
        return Optional.absent();
    }


}