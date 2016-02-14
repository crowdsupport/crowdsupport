package org.outofrange.crowdsupport.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFailureProcessor implements ApplicationListener<AuthorizationFailureEvent> {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationFailureProcessor.class);

    @Override
    public void onApplicationEvent(AuthorizationFailureEvent event) {
        log.warn("Access denied for {} (with authentication {})", event.getAuthentication().getPrincipal(), event.getAuthentication());
        log.debug("Requirements were: {}", event.getConfigAttributes());
    }
}
