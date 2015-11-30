package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.persistence.PlaceRepository;
import org.outofrange.crowdsupport.service.PlaceService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {
    @Inject
    private PlaceRepository placeRepository;

    @Override
    public Place save(Place place) {
        return placeRepository.save(place);
    }

    @Override
    public List<Place> loadAll() {
        return placeRepository.findAll();
    }

    @Override
    public Optional<Place> load(String identifier) {
        return placeRepository.findOneByIdentifier(identifier);
    }
}
