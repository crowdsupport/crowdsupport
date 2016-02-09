package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@Service
@Transactional(readOnly = true)
public class SettingsServiceImpl implements org.outofrange.crowdsupport.service.SettingsService {
    private static final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private final ConfigurationService configurationService;

    @Inject
    public SettingsServiceImpl(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasAuthority(@perm.CONFIGURE_APPLICATION)")
    public void generateNewApplicationToken() {
        log.info("Generating new application token...");

        final byte[] byteSecret = new byte[64];
        new SecureRandom().nextBytes(byteSecret);

        final String base64Secret = DatatypeConverter.printBase64Binary(byteSecret);
        configurationService.setProperty(ConfigurationService.HMAC_TOKEN_SECRET, base64Secret);

        log.warn("...refreshed application token - all issued authentication tokens are now invalid!");
    }
}
