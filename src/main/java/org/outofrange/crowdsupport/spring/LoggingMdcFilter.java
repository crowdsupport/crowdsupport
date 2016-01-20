package org.outofrange.crowdsupport.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class LoggingMdcFilter extends GenericFilterBean {
    private static final Logger log = LoggerFactory.getLogger(LoggingMdcFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String requestHash = Integer.toHexString(request.hashCode());
        MDC.put("requestHash", requestHash);

        chain.doFilter(request, response);
    }
}
