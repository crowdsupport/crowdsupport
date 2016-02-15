package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.spring.security.jwt.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CurrentUserProvider {
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
