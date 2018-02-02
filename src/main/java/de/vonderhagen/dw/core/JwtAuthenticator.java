package de.vonderhagen.dw.core;

import de.vonderhagen.dw.OauthJwtConfiguration;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

/**
 *
 * @author of7085
 */
public class JwtAuthenticator
        implements Authenticator<String, User> {

    OauthJwtConfiguration config = null;

    public JwtAuthenticator(final OauthJwtConfiguration configuration) {
        this.config = configuration;
    }

    @Override
    public Optional<User> authenticate(String jwt) throws AuthenticationException {
        Logger logger = LoggerFactory.getLogger(JwtAuthenticator.class);
        logger.trace("entering authenticate");
        // https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples
        JwtClaims jwtClaims = null;
        HttpsJwks httpsJkws = new HttpsJwks(this.config.getJwsCertUrl());
        HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer(this.config.getJwsExpectedIssuer()) // whom the JWT needs to have been issued by
                .setExpectedAudience(this.config.getJwsExpectedAudience()) // to whom the JWT is intended for
                .setVerificationKeyResolver(httpsJwksKeyResolver) // verify the signature with the public key
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        new AlgorithmConstraints(ConstraintType.WHITELIST, // which is only RS256 here
                                AlgorithmIdentifiers.RSA_USING_SHA256))
                .build(); // create the JwtConsumer instance

        try {
            //  Validate the JWT and process it to the Claims
            jwtClaims = jwtConsumer.processToClaims(jwt);
            logger.debug("JWT validation succeeded! " + jwtClaims);
        } catch (InvalidJwtException e) {
            // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
            // Hopefully with meaningful explanations(s) about what went wrong.
            logger.warn("Invalid JWT! " + e);

            // Programmatic access to (some) specific reasons for JWT invalidity is also possible
            // should you want different error handling behavior for certain conditions.
            // Whether or not the JWT has expired being one common reason for invalidity
            if (e.hasExpired()) {
                try {
                    logger.info("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
                } catch (MalformedClaimException ex) {
                    logger.warn("MalformedClaimException {}", ex.getMessage());
                }
            }

            // Or maybe the audience was invalid
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                try {
                    logger.warn("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
                } catch (MalformedClaimException ex) {
                    logger.warn("MalformedClaimException {}", ex.getMessage());
                }
            }

        }

        if (jwtClaims != null) {
            try {
                User u = new User(jwtClaims.getSubject());
                // standard claim in OpenID Connect
                if (jwtClaims.hasClaim("email")) {
                    u.setMail(jwtClaims.getClaimValue("email", String.class));
                }
                // custom claim
                if (jwtClaims.hasClaim("eduPersonScopedAffiliation")) {
                    u.setEduPersonScopedAffiliation(jwtClaims.getStringListClaimValue("eduPersonScopedAffiliation"));
                }

                Map realmAccess = jwtClaims.getClaimValue("realm_access", Map.class);
                List<String> roles = (List<String>) realmAccess.get("roles");
                for (String r : roles) {
                    logger.info("role: {}", r);
                }
                return Optional.of(u);
            } catch (MalformedClaimException ex) {
                logger.warn("MalformedClaimException {}", ex.getMessage());
            }
        }
        return Optional.empty();
    }

}
