package org.outofrange.crowdsupport.spring.profile.development;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class SpringConfigurationDevelopment {
    @Bean
    public ErrorController errorController(ErrorAttributes errorAttributes) {
        final ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setPath("/error");
        errorProperties.setIncludeStacktrace(ErrorProperties.IncludeStacktrace.ALWAYS);

        return new BasicErrorController(errorAttributes, errorProperties);
    }
}
