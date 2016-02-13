package org.outofrange.crowdsupport.util;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.spring.security.UserAuthentication;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is only for testing purposes and is meant to execute service which require certain authorities.
 * <br/>
 * The mapping between roles and {@link GrantedAuthority}s is done in {@link AuthorityResolver}
 */
public class Authorized {
    private Authentication authentication;

    private Authorized(Set<GrantedAuthority> authorities) {
        authentication = new TestingAuthenticationToken(null, null, new ArrayList<>(authorities));
        authentication.setAuthenticated(true);
    }

    private Authorized(User user) {
        authentication = new UserAuthentication(user);
        authentication.setAuthenticated(true);
    }

    public static Authorized asAdmin() {
        return withRoles(RoleStore.ADMIN, RoleStore.USER);
    }

    public static Authorized asNormal() {
        return withRoles(RoleStore.USER);
    }

    public static Authorized asUser(User user) {
        return new Authorized(user);
    }

    public static Authorized withRoles(String... roles) {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        for (String roleName : roles) {
            authorities.addAll(AuthorityResolver.resolveRoles(roleName));
        }

        return new Authorized(authorities);
    }

    public void run(Runnable task) {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication originalAuthentication = context.getAuthentication();

        context.setAuthentication(authentication);
        task.run();
        context.setAuthentication(originalAuthentication);
    }
}
