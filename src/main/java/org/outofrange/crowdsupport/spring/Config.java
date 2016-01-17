package org.outofrange.crowdsupport.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;

@Component
public class Config {
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
}
