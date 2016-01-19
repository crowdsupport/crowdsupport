package org.outofrange.crowdsupport.spring;

import org.outofrange.crowdsupport.util.CsModelMapper;
import org.outofrange.crowdsupport.util.PermissionStore;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.inject.Inject;
import javax.servlet.DispatcherType;

@Configuration
public class SpringConfiguration {
    @Inject
    private Config config;

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
        // TODO make configurable
        if (config.isDebugEnabled()) {
            registrationBean.addInitParameter("logLevel", "DEBUG");
        }

        // we have to do our rewriting first, before security sees us /o\
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
        return registrationBean;
    }

    @Bean
    public Config config() {
        return new Config();
    }
}
