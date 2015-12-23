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
    public Optional<Place> load(String stateIdentifier, String cityIdentifier, String placeIdentifier) {
        final Optional<Place> place = placeRepository.findOneByIdentifier(placeIdentifier);

        if (place.isPresent() && cityIdentifier.equals(place.get().getCity().getIdentifier()) &&
                stateIdentifier.equals(place.get().getCity().getState().getIdentifier())) {
            return place;
        } else {
            return Optional.empty();
        }
    }
}
