package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.persistence.PlaceRequestRepository;
import org.outofrange.crowdsupport.service.*;
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
    public List<PlaceRequest> loadAllWithInactivePlace() {
        return placeRequestRepository.findByPlaceActiveFalse();
    }

    @Override
    public PlaceRequest requestNewPlace(PlaceRequest placeRequest) {
        log.trace("Requesting new place: {}", placeRequest);

        if (placeRequest.getPlace().getCity() != null) {
            final String cityIdentifier = placeRequest.getPlace().getCity().getIdentifier();
            final String stateIdentifier = placeRequest.getPlace().getCity().getState().getIdentifier();
            final Optional<City> city = cityService.load(cityIdentifier, stateIdentifier);

            if (city.isPresent()) {
                log.trace("Found existing city attached to place request: {}", city.get());
                placeRequest.getPlace().setCity(city.get());
            } else {
                throw new ServiceException("Couldn't find city with identifier " + cityIdentifier);
            }
        }

        placeRequest.getPlace().setActive(false);
        placeRequest.setPlace(placeService.save(placeRequest.getPlace()));

        return save(placeRequest);
    }

    @Override
    public PlaceRequest saveNewPlace(PlaceRequest placeRequest) {
        log.trace("Saving new place {}", placeRequest);

        final Optional<City> cityDb = cityService.load(placeRequest.getPlace().getCity().getIdentifier(),
                placeRequest.getPlace().getCity().getState().getIdentifier());
        if (!cityDb.isPresent()) {
            throw new ServiceException("Found no city with identifier " + placeRequest.getPlace().getCity().getIdentifier());
        }
        if (!cityDb.get().equals(placeRequest.getPlace().getCity())) {
            throw new ServiceException("City in database differs from passed city!");
        }

        final PlaceRequest placeRequestDb = placeRequestRepository.findOne(placeRequest.getId());

        placeRequestDb.getPlace().setCity(cityDb.get());
        placeRequestDb.getPlace().setActive(true);

        return save(placeRequestDb);
    }

    @Override
    public void declinePlaceRequest(Long placeRequestId) {
        log.debug("Declining place request with id {}", placeRequestId);

        final Optional<PlaceRequest> placeRequestDb = Optional.ofNullable(placeRequestRepository.findOne(placeRequestId));

        if (!placeRequestDb.isPresent()) {
            throw new ServiceException("Couldn't find place request to delete!");
        }
        if (placeRequestDb.get().getPlace().isActive()) {
            throw new ServiceException("Place is already active!");
        }

        deletePlaceRequest(placeRequestDb.get());
        placeService.deletePlace(placeRequestDb.get().getPlace());
    }

    public void deletePlaceRequest(PlaceRequest placeRequest) {
        log.debug("Deleting place request {}", placeRequest);

        placeRequestRepository.delete(placeRequest);
    }
}
