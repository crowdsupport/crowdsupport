package org.outofrange.crowdsupport.service;

public interface ConfigurationService {
    String HMAC_TOKEN_SECRET = "crowdsupport.token.secret";

    String getProperty(String key);

    String setProperty(String key, String value);

    void clearCache();
}
