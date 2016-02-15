package org.outofrange.crowdsupport.spring.api;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * This custom request mapping builds API mappings considering {@link ApiVersion}.
 */
public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    // this code example has been found at Stackoverflow: http://stackoverflow.com/a/21176971/1436981
    // but it had to be adapted to work correct with Hibernate
    // see also: http://stackoverflow.com/questions/20198275/how-to-manage-rest-api-versioning-with-spring#comment57633565_21176971

    /**
     * The prefix to use for all mappings
     */
    private final String prefix;

    /**
     * Builds a new handler mapping with a specified prefix.
     *
     * @param prefix the prefix to use for all mappings
     */
    public ApiVersionRequestMappingHandlerMapping(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        // get default mapping from super
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if (methodAnnotation != null) {
            // method is annotated with ApiVersion

            // Concatenate ApiVersion with the usual request mapping
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            info = createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
        } else {
            // method isn't annotated with ApiVersion

            if (method.isAnnotationPresent(RequestMapping.class)) {
                // but it's still annotated with RequestMapping!

                ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
                if (typeAnnotation != null) {
                    // and ApiVersion is present at type level

                    // Concatenate our ApiVersion with the usual request mapping
                    RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                    info = createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
                }
            }
        }

        return info;
    }

    private RequestMappingInfo createApiVersionInfo(ApiVersion annotation, RequestCondition<?> customCondition) {
        String[] values = annotation.value();
        String[] patterns = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            // Build the URL prefix
            patterns[i] = prefix + values[i];
        }

        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }

}