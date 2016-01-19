package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.persistence.PlaceRepository;
import org.outofrange.crowdsupport.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {
    private static final Logger log = LoggerFactory.getLogger(PlaceServiceImpl.class);

    @Inject
    private PlaceRepository placeRepository;

    @Override
    public Place save(Place place) {
        log.debug("Saving place {}", place);

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

    @Override
    public List<Place> loadActivePlaces(String identifier) {
        log.trace("Loading active places for city with identifier {}", identifier);

        return placeRepository.findByActiveTrueAndCityIdentifier(identifier);
    }

    @Override
    public void deletePlace(Place place) {
        log.debug("Deleting place {}", place);
        placeRepository.delete(place);
    }
}
