package de.vonderhagen.dw.core;

import io.dropwizard.auth.Authorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author of7085
 */
public class ExampleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        Logger logger = LoggerFactory.getLogger(ExampleAuthorizer.class);
        logger.warn("authorize {} {}", user.getName(), role);
        if (user.getEduPersonScopedAffiliation().contains("member@kit.edu") && role.equals("ORGMEMBER")) {
            return true;
        }

        return user.getName().equals("3ac78c01-9a9f-440b-a7cc-636e02d9e45f") && role.equals("OAUTH");
    }
}
