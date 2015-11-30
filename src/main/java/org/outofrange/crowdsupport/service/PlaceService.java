package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.persistence.PlaceRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PlaceService {
    @Inject
    private PlaceRepository placeRepository;

    public Place save(Place place) {
        return placeRepository.save(place);
    }
}
