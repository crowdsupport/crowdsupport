package org.outofrange.crowdsupport.spring;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.spring.logging.RequestLoggingUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.DispatcherType;

@Configuration
public class SpringConfiguration {
    @Value("${crowdsupport.tuckey.debug}")
    private Boolean tuckeyDebug;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new TuckeyFilter());
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("confPath", "urlrewrite.xml");

        if (tuckeyDebug != null && tuckeyDebug) {
            registrationBean.addInitParameter("logLevel", "DEBUG");
        }

        // we have to do our rewriting first, before security sees us /o\
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
        return registrationBean;
    }

    @Bean
    public RequestLoggingUtility requestLoggingUtility() {
        return new RequestLoggingUtility();
    }

    @Bean
    public FilterRegistrationBean loggingMdcFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(requestLoggingUtility());

        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/service/*");

        return registrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean<RequestLoggingUtility> loggingMdcListener() {
        return new ServletListenerRegistrationBean<>(requestLoggingUtility());
    }

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    /*@Bean
    @Inject
    public Config config(ServletContext servletContext) {
        return new Config(servletContext);
    }*/
}
