package org.outofrange.crowdsupport.util;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class is only for testing purposes and is meant to execute service which require certain authorities.
 * <br/>
 * The mapping between roles and {@link GrantedAuthority}s is done in {@link AuthorityResolver}
 */
public class Authorized {
    private static final ModelMapper mapper = new ModelMapper();

    private Authentication authentication;

    private Authorized(Set<GrantedAuthority> authorities) {
        authentication = new TestingAuthenticationToken(null, null, new ArrayList<>(authorities));
        authentication.setAuthenticated(true);
    }

    public static Authorized asAdmin() {
        return withRoles(RoleStore.ADMIN, RoleStore.USER);
    }

    public static Authorized asNormal() {
        return withRoles(RoleStore.USER);
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

    public <T> T run(Supplier<T> task) {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication originalAuthentication = context.getAuthentication();

        context.setAuthentication(authentication);
        T result = task.get();
        context.setAuthentication(originalAuthentication);

        return result;
    }
}
