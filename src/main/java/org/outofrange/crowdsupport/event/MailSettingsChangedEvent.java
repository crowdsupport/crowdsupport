package org.outofrange.crowdsupport.event;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;

/**
 * An event indicating that the mail settings have been changed.
 */
public class MailSettingsChangedEvent implements Event {
    private final MailSettingsDto settings;

    /**
     * Creates a new event with the passed settings
     *
     * @param settings the settings to publish along with the event
     */
    public MailSettingsChangedEvent(MailSettingsDto settings) {
        this.settings = settings;
    }

    /**
     * Returns the new mail settings.
     *
     * @return the nwe mail settings
     */
    public MailSettingsDto getSettings() {
        return settings;
    }
}
