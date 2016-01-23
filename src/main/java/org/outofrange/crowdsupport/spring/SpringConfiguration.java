package org.outofrange.crowdsupport.spring;

import org.outofrange.crowdsupport.spring.logging.RequestLoggingUtility;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;

@Configuration
public class SpringConfiguration {
    private RequestLoggingUtility requestLoggingUtility = new RequestLoggingUtility();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new UrlRewriteFilter());
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("confPath", "urlrewrite.xml");

        // we have to do our rewriting first, before security sees us /o\
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean loggingMdcFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(requestLoggingUtility);

        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/service/*");

        return registrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean<RequestLoggingUtility> loggingMdcListener() {
        return new ServletListenerRegistrationBean<>(requestLoggingUtility);
    }

    @Bean
    @Inject
    public Config config(ServletContext servletContext) {
        return new Config(servletContext);
    }
}
