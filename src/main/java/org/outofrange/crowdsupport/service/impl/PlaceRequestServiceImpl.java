package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.persistence.PlaceRequestRepository;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.service.PlaceRequestService;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceRequestServiceImpl implements PlaceRequestService {
    private static final Logger log = LoggerFactory.getLogger(PlaceRequestServiceImpl.class);

    @Inject
    private PlaceRequestRepository placeRequestRepository;

    @Inject
    private CityService cityService;

    @Inject
    private PlaceService placeService;

    @Override
    public PlaceRequest save(PlaceRequest entity) {
        log.trace("Saving place request: {}", entity);
        return placeRequestRepository.save(entity);
    }

    @Override
    public List<PlaceRequest> loadAll() {
        return placeRequestRepository.findAll();
    }

    @Override
    public PlaceRequest requestNewPlace(PlaceRequest placeRequest) {
        log.trace("Requesting new place: {}", placeRequest);

        if (placeRequest.getPlace().getCity() != null) {
            final String cityIdentifier = placeRequest.getPlace().getCity().getIdentifier();
            final Optional<City> city = cityService.load(cityIdentifier);

            if (city.isPresent()) {
                log.trace("Found existing city attached to place request: {}", city.get());
                placeRequest.getPlace().setCity(city.get());
            } else {
                throw new ServiceException("Couldn't find city with identifier " + cityIdentifier);
            }
        }

        placeRequest.setPlace(placeService.save(placeRequest.getPlace()));

        return save(placeRequest);
    }
}
