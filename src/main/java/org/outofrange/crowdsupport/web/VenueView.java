package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.service.VenueController;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class VenueView {
    @Inject
    private VenueController venueController;
}
