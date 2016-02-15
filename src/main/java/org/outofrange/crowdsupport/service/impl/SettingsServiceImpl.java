package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;
import org.outofrange.crowdsupport.dto.config.SettingsDto;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.service.ConfigurationService;
import org.outofrange.crowdsupport.service.SettingsService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@Service
@Transactional(readOnly = true)
public class SettingsServiceImpl implements SettingsService {
    private static final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private final ConfigurationService configurationService;
    private final MailSenderImpl mailSender;

    @Inject
    public SettingsServiceImpl(ConfigurationService configurationService, MailSenderImpl mailSender) {
        this.configurationService = configurationService;
        this.mailSender = mailSender;
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

    @Override
    @PreAuthorize("hasAuthority(@perm.CONFIGURE_APPLICATION)")
    public SettingsDto getSettings() {
        log.debug("Loading settings");

        final SettingsDto dto = new SettingsDto();
        dto.setMail(getMailSettings());

        return dto;
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.CONFIGURE_APPLICATION)")
    public MailSettingsDto getMailSettings() {
        log.debug("Loading mail settings");

        final MailSettingsDto dto = new MailSettingsDto();
        dto.setEnabled(Boolean.valueOf(configurationService.getProperty(ConfigurationService.MAIL_ENABLED)));
        dto.setFrom(configurationService.getProperty(ConfigurationService.MAIL_FROM));
        dto.setHost(configurationService.getProperty(ConfigurationService.SMTP_HOST));
        dto.setPort(configurationService.getProperty(ConfigurationService.SMTP_PORT));
        dto.setUser(configurationService.getProperty(ConfigurationService.SMTP_USER));
        dto.setPass(configurationService.getProperty(ConfigurationService.SMTP_PASS));

        return dto;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasAuthority(@perm.CONFIGURE_APPLICATION)")
    public void setMailSettings(MailSettingsDto mailSettings) {
        log.debug("Setting mail settings to {}", mailSettings);

        if (mailSettings.getEnabled() != null) {
            configurationService.setProperty(ConfigurationService.MAIL_ENABLED, mailSettings.getEnabled().toString());
        }
        configurationService.setProperty(ConfigurationService.MAIL_FROM, mailSettings.getFrom());
        configurationService.setProperty(ConfigurationService.SMTP_HOST, mailSettings.getHost());
        configurationService.setProperty(ConfigurationService.SMTP_PORT, mailSettings.getPort());
        configurationService.setProperty(ConfigurationService.SMTP_USER, mailSettings.getUser());
        configurationService.setProperty(ConfigurationService.SMTP_PASS, mailSettings.getPass());

        Events.mailSettingsChanged(mailSettings).publish();
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.CONFIGURE_APPLICATION)")
    public void testMailSettings() {
        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new ServiceException("Could not connect to server", e);
        }
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.CONFIGURE_APPLICATION)")
    public void sendTestMail(String email) {
        log.info("Sending test email to {}", email);

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(configurationService.getProperty(ConfigurationService.SMTP_PASS));
        message.setSubject("Crowdsupport Test Mail Subject");
        message.setText("Crowdsupport Test Mail Text");

        mailSender.send(message);
    }
}
