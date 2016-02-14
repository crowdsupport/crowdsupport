package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;
import org.outofrange.crowdsupport.dto.config.SettingsDto;

public interface SettingsService {
    void generateNewApplicationToken();

    SettingsDto getSettings();

    MailSettingsDto getMailSettings();

    void setMailSettings(MailSettingsDto mailSettings);

    void testMailSettings();

    void sendTestMail(String email);
}
