package org.outofrange.crowdsupport.service;

public interface ConfigurationService {
    String HMAC_TOKEN_SECRET = "crowdsupport.token.secret";

    String MAIL_ENABLED = "crowdsupport.mail.enabled";
    String MAIL_FROM = "crowdsupport.mail.from";
    String SMTP_HOST = "crowdsupport.smtp.host";
    String SMTP_PORT = "crowdsupport.smtp.port";
    String SMTP_USER = "crowdsupport.smtp.user";
    String SMTP_PASS = "crowdsupport.smtp.pass";

    /**
     * Returns the value of property.
     *
     * @param key the key of the property
     * @return the value of the property
     */
    String getProperty(String key);

    /**
     * Sets the value of a property
     *
     * @param key   the key of the property
     * @param value the value of the property
     * @return the previous value stored in the property, or null if none was stored
     */
    String setProperty(String key, String value);

    /**
     * Clears the property cache.
     */
    void clearCache();
}
