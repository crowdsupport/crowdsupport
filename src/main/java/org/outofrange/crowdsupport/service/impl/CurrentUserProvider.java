package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.spring.security.jwt.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * This class simply provides access to the currently authenticated user.
 */
public class CurrentUserProvider {
    /**
     * Returns the username of the currently authenticated user.
     *
     * @return the username of the currently authenticated user, or {@link Optional#empty()} if none could be found
     */
    public Optional<String> getCurrentUsername() {
        final Object userAuth = SecurityContextHolder.getContext().getAuthentication();

        if (userAuth instanceof UserAuthentication) {
            final UserAuthentication ua = (UserAuthentication) userAuth;

            return Optional.of(ua.getName());
        } else {
            return Optional.empty();
        }
    }
}
