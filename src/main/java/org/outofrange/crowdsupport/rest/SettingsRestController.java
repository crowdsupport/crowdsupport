package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.service.SettingsService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/refreshApplicationSecret", method = RequestMethod.POST)
    public ResponseEntity<Void> renewApplicationSecret() {
        log.info("Refreshing application secret!");

        settingsService.generateNewApplicationToken();

        return ResponseEntity.ok().build();
    }
}
