package de.vonderhagen.dw;

import de.vonderhagen.dw.core.ExampleAuthorizer;
import de.vonderhagen.dw.core.JwtAuthenticator;
import de.vonderhagen.dw.core.User;
import de.vonderhagen.dw.resources.ProtectedResource;
import de.vonderhagen.dw.resources.PublicResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OauthJwtApplication extends Application<OauthJwtConfiguration> {

    public static void main(final String[] args) throws Exception {
        new OauthJwtApplication().run(args);
    }

    @Override
    public String getName() {
        return "oauthJws";
    }

    @Override
    public void initialize(final Bootstrap<OauthJwtConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final OauthJwtConfiguration configuration,
            final Environment environment) {
        final PublicResource pub = new PublicResource(configuration.getPublicResponse());
        environment.jersey().register(pub);
        final ProtectedResource pro = new ProtectedResource(configuration.getProtectedResponse());
        environment.jersey().register(pro);

        final FilterRegistration.Dynamic corsFilter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
                "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, configuration.getCorsOrigins());
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new JwtAuthenticator(configuration))
                .setAuthorizer(new ExampleAuthorizer())
                .setPrefix("Bearer")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

}
