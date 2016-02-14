package org.outofrange.crowdsupport.service;

public interface ConfigurationService {
    String HMAC_TOKEN_SECRET = "crowdsupport.token.secret";

    String MAIL_ENABLED = "crowdsupport.mail.enabled";
    String MAIL_FROM = "crowdsupport.mail.from";
    String SMTP_HOST = "crowdsupport.smtp.host";
    String SMTP_PORT = "crowdsupport.smtp.port";
    String SMTP_USER = "crowdsupport.smtp.user";
    String SMTP_PASS = "crowdsupport.smtp.pass";

    String getProperty(String key);

    String setProperty(String key, String value);

    void clearCache();
}
