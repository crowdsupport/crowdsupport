package org.outofrange.crowdsupport.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.outofrange.crowdsupport.spring.api.ApiVersionRequestMappingHandlerMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.inject.Inject;
import java.util.List;

/**
 * Configuration class to register custom {@link ApiVersionRequestMappingHandlerMapping} and set caching for resources.
 */
@Configuration
public class WebMvcConfig extends DelegatingWebMvcConfiguration {
    private static final int FOURTY_FIVE_DAYS = 60 * 60 * 24 * 45;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(FOURTY_FIVE_DAYS);
        registry.addResourceHandler("/r/**").addResourceLocations("classpath:/static/r/").setCachePeriod(FOURTY_FIVE_DAYS);
    }

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping("/service/v");
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        addDefaultHttpMessageConverters(converters);

        converters.stream()
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .forEach(converter -> {
                    MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                    jacksonConverter.setObjectMapper(objectMapper);
                });
    }
}
