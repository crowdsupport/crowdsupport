package org.outofrange.crowdsupport.spring.logging;

import org.outofrange.crowdsupport.spring.logging.RequestLoggingUtility;
import org.slf4j.MDC;
import org.springframework.boot.logging.logback.ColorConverter;

public class ColorFromRequestConverter extends ColorConverter {
    @Override
    public String getFirstOption() {
        return MDC.get(RequestLoggingUtility.COLOR_PROPERTY);
    }
}
