package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.service.WelcomeController;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class WelcomeView {
    @Inject
    private WelcomeController welcomeController;
}
