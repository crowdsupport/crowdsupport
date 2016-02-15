package org.outofrange.crowdsupport.util;

import org.springframework.stereotype.Component;

/**
 * Stores strings representations of all {@link org.outofrange.crowdsupport.model.Role}s and exposing them as a bean.
 */
@Component("role")
public class RoleStore {
    /**
     * Makes an user to an admin; contains all {@link org.outofrange.crowdsupport.model.Permission}s.
     */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Default role every user has. Is necessary to be allowed to log in.
     */
    public static final String USER = "ROLE_USER";
}
