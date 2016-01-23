package org.outofrange.crowdsupport.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Arrays;

@Component
public class Config {
    public static ServletContext servletContext;

    @Inject
    public Config(ServletContext servletContext) {
        Config.servletContext = servletContext;
    }

    @Value("${crowdsupport.version}")
    private String version;

    @Inject
    private Environment environment;

    public boolean isDebugEnabled() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }

    public String getVersion() {
        return version;
    }

    public static String getContextPath() {
        return servletContext != null ? servletContext.getContextPath() : null;
    }
}
