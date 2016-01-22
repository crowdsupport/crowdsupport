package org.outofrange.crowdsupport.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class WebMvcConfig extends DelegatingWebMvcConfiguration {
    private static final int FOURTY_FIVE_DAYS = 60 * 60 * 24 * 45;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/r/**").addResourceLocations("classpath:/static/r/").setCachePeriod(FOURTY_FIVE_DAYS);
    }

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping("/service/v");
    }
}
