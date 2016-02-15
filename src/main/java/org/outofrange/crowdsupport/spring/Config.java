package org.outofrange.crowdsupport.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Arrays;

/**
 * Configuration class that exposes some beans and properties.
 */
@Component
public class Config {
    private static ServletContext servletContext;

    private final String version;

    private final Environment environment;

    @Inject
    public Config(ServletContext servletContext, Environment environment,
                  @Value("${crowdsupport.version}") String version) {
        Config.servletContext = servletContext;
        this.environment = environment;
        this.version = version;
    }

    /**
     * Checks if Spring is running in development mode.
     *
     * @return if Spring is running in mode &quot;{@code dev}&quot;
     */
    public boolean isDebugEnabled() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }

    /**
     * Returns the current version of the application.
     *
     * @return the current version of the application
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the current context path of the running application.
     *
     * @return the current context path of the running application
     */
    public static String getContextPath() {
        return servletContext != null ? servletContext.getContextPath() : null;
    }
}
