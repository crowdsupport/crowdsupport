package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.service.CityController;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CityOverviewView {
    @Inject
    private CityController cityController;
}
