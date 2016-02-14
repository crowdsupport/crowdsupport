package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;

public class MailSettingsChangedEvent implements Event {
    private final MailSettingsDto settings;

    public MailSettingsChangedEvent(MailSettingsDto settings) {
        this.settings = settings;
    }

    public MailSettingsDto getSettings() {
        return settings;
    }
}
