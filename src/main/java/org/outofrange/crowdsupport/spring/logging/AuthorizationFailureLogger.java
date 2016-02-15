package org.outofrange.crowdsupport.spring.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.stereotype.Component;

/**
 * Listens to authorization failure events, caused by missing authentication when checking method security, and logs
 * failed attempts.
 */
@Component
public class AuthorizationFailureLogger implements ApplicationListener<AuthorizationFailureEvent> {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationFailureLogger.class);

    @Override
    public void onApplicationEvent(AuthorizationFailureEvent event) {
        log.warn("Access denied for {} (with authentication {})", event.getAuthentication().getPrincipal(), event.getAuthentication());
        log.debug("Requirements were: {}", event.getConfigAttributes());
    }
}
