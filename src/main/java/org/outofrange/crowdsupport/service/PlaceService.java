package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Place;

import java.util.Optional;

public interface PlaceService extends CrowdsupportService<Place> {
    Optional<Place> load(String identifier);
}
