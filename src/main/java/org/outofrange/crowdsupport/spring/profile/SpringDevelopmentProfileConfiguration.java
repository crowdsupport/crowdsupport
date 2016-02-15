package org.outofrange.crowdsupport.spring.profile;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This configuration is being taken into account when starting Spring with the dev profile.
 * <p/>
 * It provides a custom error controller to make debugging easier.
 */
@Configuration
@Profile("dev")
public class SpringDevelopmentProfileConfiguration {
    @Bean
    public ErrorController errorController(ErrorAttributes errorAttributes) {
        final ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setPath("/error");
        errorProperties.setIncludeStacktrace(ErrorProperties.IncludeStacktrace.ALWAYS);

        return new BasicErrorController(errorAttributes, errorProperties);
    }
}
