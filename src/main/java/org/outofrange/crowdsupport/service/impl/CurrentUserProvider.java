package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.spring.security.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CurrentUserProvider {
    public Optional<User> getCurrentUser() {
        final Object userAuth = SecurityContextHolder.getContext().getAuthentication();

        if (userAuth instanceof UserAuthentication) {
            final UserAuthentication u = (UserAuthentication) userAuth;
            return Optional.of(u.getDetails());
        } else {
            return Optional.empty();
        }
    }
}
