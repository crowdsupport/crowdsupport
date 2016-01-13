package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.Place;
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
    private StateService stateService;

    @Inject
    private PlaceService placeService;

    @Inject
    private UserService userService;

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
            final Optional<City> city = cityService.load(cityIdentifier);

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

        final PlaceRequest placeRequestFromDb = placeRequestRepository.findByPlaceIdentifier(placeRequest.getPlace().getIdentifier()).get();
        final Place placeFromDb = placeRequestFromDb.getPlace();

        // save save save
        final Optional<City> cityFromDb = cityService.load(placeRequest.getPlace().getCity().getIdentifier());
        if (cityFromDb.isPresent()) {
            if (cityFromDb.get().equals(placeRequest.getPlace().getCity())) {
                placeFromDb.setCity(cityFromDb.get());
            } else {
                throw new ServiceException("Found existing city for identifier " + placeRequest.getPlace().getCity().getIdentifier());
            }
        } else {
            placeFromDb.setCity(placeRequest.getPlace().getCity());

            placeFromDb.getCity().setState(stateService.saveOrRetrieveState(placeRequest.getPlace().getCity().getState()));
            placeFromDb.setCity(cityService.saveOrRetrieveCity(placeRequest.getPlace().getCity()));
        }

        placeFromDb.setActive(true);
        placeRequest.setPlace(placeService.save(placeFromDb));
        placeRequest.setRequestingUser(userService.loadUserByUsername(placeRequest.getRequestingUser().getUsername()));

        return save(placeRequest);
    }
}
