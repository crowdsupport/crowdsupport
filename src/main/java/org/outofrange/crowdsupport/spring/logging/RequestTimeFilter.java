package org.outofrange.crowdsupport.spring.logging;

import org.slf4j.MDC;
import org.springframework.boot.logging.logback.ColorConverter;

public class RequestTimeFilter extends ColorConverter {
    @Override
    public String getFirstOption() {
        return MDC.get(RequestLoggingUtility.COLOR_PROPERTY);
    }
}
