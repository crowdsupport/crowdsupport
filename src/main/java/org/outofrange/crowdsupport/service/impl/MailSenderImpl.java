package org.outofrange.crowdsupport.service.impl;

import com.google.common.base.Charsets;
import com.google.common.eventbus.Subscribe;
import org.outofrange.crowdsupport.dto.config.MailSettingsDto;
import org.outofrange.crowdsupport.event.EventDispatcher;
import org.outofrange.crowdsupport.event.MailSettingsChangedEvent;
import org.outofrange.crowdsupport.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.MessagingException;
import java.util.Properties;

@Service("mailSender")
public class MailSenderImpl implements MailSender {
    private static final Logger log = LoggerFactory.getLogger(MailSenderImpl.class);

    private static final Properties JAVA_MAIL_PROPERTIES = new Properties();
    private final ConfigurationService configurationService;

    static {
        JAVA_MAIL_PROPERTIES.setProperty("mail.smtp.auth", "true");
        JAVA_MAIL_PROPERTIES.setProperty("mail.smtp.starttls.enable", "true");
    }

    private JavaMailSenderImpl sender;
    private boolean connectionTested = false;

    @Inject
    public MailSenderImpl(ConfigurationService config) {
        EventDispatcher.register(this);

        this.configurationService = config;

        final String smtpPort = config.getProperty(ConfigurationService.SMTP_PORT);
        final String smtpHost = config.getProperty(ConfigurationService.SMTP_HOST);
        final String smtpUser = config.getProperty(ConfigurationService.SMTP_USER);
        final String smtpPass = config.getProperty(ConfigurationService.SMTP_PASS);

        if (smtpHost != null && smtpPort != null && smtpUser != null && smtpPass != null) {
            final JavaMailSenderImpl springSender = new JavaMailSenderImpl();

            springSender.setHost(smtpHost);
            springSender.setPort(Integer.valueOf(smtpPort));
            springSender.setUsername(smtpUser);
            springSender.setPassword(smtpPass);
            springSender.setJavaMailProperties(JAVA_MAIL_PROPERTIES);

            springSender.setDefaultEncoding(Charsets.UTF_8.name());

            sender = springSender;
        }
    }

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        if (sender != null) {
            log.info("Sending mail: {}", simpleMailMessage);
            sender.send(simpleMailMessage);
        } else {
            log.warn("Should've sent mail, but sender isn't configured: {}", simpleMailMessage);
        }
    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
        if (sender != null) {
            log.info("Sending mails: {}", simpleMailMessages);
            sender.send(simpleMailMessages);
        } else {
            log.warn("Should've sent mails, but sender isn't configured: {}", simpleMailMessages);
        }
    }

    @Subscribe
    public void reloadSender(MailSettingsChangedEvent event) {
        final MailSettingsDto newSettings = event.getSettings();
        if (newSettings != null && newSettings.getEnabled()) {
            sender = createMailSender(newSettings.getHost(), Integer.valueOf(newSettings.getPort()),
                    newSettings.getUser(), newSettings.getPass());
        } else {
            sender = null;
            connectionTested = false;
        }
    }

    public void testConnection() throws MessagingException {
        if (sender == null) {
            throw new MessagingException("Sender isn't configured yet!");
        }

        log.info("Testing connection to server {} ...", sender.getHost());
        sender.testConnection();
        log.info("...could connect to server!");
        connectionTested = true;
    }

    public boolean isConnectionTested() {
        return connectionTested;
    }

    public SimpleMailMessage getNewMessage(String to) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(configurationService.getProperty(ConfigurationService.MAIL_FROM));
        message.setTo(to);

        return message;
    }

    private static JavaMailSenderImpl createMailSender(String host, int port, String user, String pass) {
        final JavaMailSenderImpl springSender = new JavaMailSenderImpl();

        springSender.setHost(host);
        springSender.setPort(port);
        springSender.setUsername(user);
        springSender.setPassword(pass);
        springSender.setJavaMailProperties(JAVA_MAIL_PROPERTIES);
        springSender.setDefaultEncoding(Charsets.UTF_8.name());

        return springSender;
    }
}
