package org.outofrange.crowdsupport.spring;

import org.outofrange.crowdsupport.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

/**
 * This service contains tasks done when the application has been started.
 */
@Service
public class ApplicationInitialization {
    private static final Logger log = LoggerFactory.getLogger(ApplicationInitialization.class);

    private final ConfigurationService configurationService;

    @Inject
    public ApplicationInitialization(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Creates a new secret token if none could be found.
     */
    @PostConstruct
    public void initializeSecretToken() {
        String base64Secret = configurationService.getProperty(ConfigurationService.HMAC_TOKEN_SECRET);

        if (base64Secret == null) {
            log.info("Creating new authentication secret");

            final byte[] byteSecret = new byte[64];
            new SecureRandom().nextBytes(byteSecret);

            base64Secret = DatatypeConverter.printBase64Binary(byteSecret);
            configurationService.setProperty(ConfigurationService.HMAC_TOKEN_SECRET, base64Secret);
        }
    }
}
