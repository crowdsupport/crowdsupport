package org.outofrange.crowdsupport.spring.logging;

import org.slf4j.MDC;
import org.springframework.boot.logging.logback.ColorConverter;

/**
 * Custom logback converter to read the color from {@link MDC} instead of the log pattern.
 */
public class RequestColorConverter extends ColorConverter {
    @Override
    public String getFirstOption() {
        return MDC.get(RequestLoggingUtility.COLOR_PROPERTY);
    }
}
