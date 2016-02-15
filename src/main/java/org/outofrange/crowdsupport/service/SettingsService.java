package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;
import org.outofrange.crowdsupport.dto.config.SettingsDto;

public interface SettingsService {
    /**
     * Generates a new application token used for issuing new JWTs.
     * <p>
     * After executing this method, all users have to log in again, as it will invalidate all existing tokens.
     */
    void generateNewApplicationToken();

    /**
     * Returns all application settings.
     *
     * @return all application settings
     */
    SettingsDto getSettings();

    /**
     * Returns all mail settings
     *
     * @return all mail settings
     */
    MailSettingsDto getMailSettings();

    /**
     * Sets mail settings
     *
     * @param mailSettings the mail settings to set
     */
    void setMailSettings(MailSettingsDto mailSettings);

    /**
     * Tests if the configured mail sender can connect to the SMTP serve
     *
     * @throws ServiceException if no connection was possible
     */
    void testMailSettings();

    /**
     * Sends a test email to the given address
     *
     * @param email the email address to send the mail to
     */
    void sendTestMail(String email);
}
