package org.outofrange.crowdsupport.spring;

import org.outofrange.crowdsupport.util.CsModelMapper;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.DispatcherType;

@Configuration
public class SpringConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsModelMapper mapper() {
        return new CsModelMapper();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new UrlRewriteFilter());
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("confPath", "urlrewrite.xml");
        // TODO make configurable
        registrationBean.addInitParameter("logLevel", "DEBUG");
        registrationBean.addInitParameter("confReloadCheckInterval", "0");

        return registrationBean;
    }
}
