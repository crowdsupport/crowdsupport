package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.config.MailSettingsDto;
import org.outofrange.crowdsupport.dto.config.SettingsDto;
import org.outofrange.crowdsupport.service.SettingsService;
import org.outofrange.crowdsupport.spring.api.ApiVersion;
import org.outofrange.crowdsupport.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@ApiVersion("1")
@RequestMapping(value = "/setting")
public class SettingsRestController {
    private static final Logger log = LoggerFactory.getLogger(SettingsRestController.class);

    private final SettingsService settingsService;

    @Inject
    public SettingsRestController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SettingsDto> getSettings() {
        log.info("Querying settings");

        return ResponseEntity.ok(settingsService.getSettings());
    }

    @RequestMapping(value = "/refreshApplicationSecret", method = RequestMethod.POST)
    public ResponseEntity<Void> renewApplicationSecret() {
        log.info("Refreshing application secret!");

        settingsService.generateNewApplicationToken();

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public ResponseEntity<Void> changeMailSettings(@RequestBody MailSettingsDto mailSettings) {
        log.info("Changing mail settings");

        final MailSettingsDto oldSettings = settingsService.getMailSettings();

        settingsService.setMailSettings(mailSettings);
        if (mailSettings.getEnabled()) {
            try {
                settingsService.testMailSettings();
            } catch (ServiceException e) {
                settingsService.setMailSettings(oldSettings);
                throw e;
            }
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/sendTestEmail", method = RequestMethod.POST)
    public ResponseEntity<Void> sentTestEmail(@RequestBody String email) {
        log.info("Sending test email");

        settingsService.sendTestMail(email);

        return ResponseEntity.ok().build();
    }
}
