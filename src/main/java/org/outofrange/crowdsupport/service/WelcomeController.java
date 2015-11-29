package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.persistence.CityRepository;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller
public class WelcomeController {
    @Inject
    private CityRepository cityRepository;
}
