package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.PlaceRequest;
import org.outofrange.crowdsupport.model.Team;
import org.outofrange.crowdsupport.persistence.CityRepository;
import org.outofrange.crowdsupport.persistence.PlaceRequestRepository;
import org.outofrange.crowdsupport.service.PlaceRequestService;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceRequestServiceImpl implements PlaceRequestService {
    private static final Logger log = LoggerFactory.getLogger(PlaceRequestServiceImpl.class);

    private final PlaceRequestRepository placeRequestRepository;
    private final CityRepository cityRepository;
    private final PlaceService placeService;
    private final UserService userService;

    @Inject
    public PlaceRequestServiceImpl(PlaceRequestRepository placeRequestRepository, CityRepository cityRepository,
                                   PlaceService placeService, UserService userService) {
        this.placeRequestRepository = placeRequestRepository;
        this.cityRepository = cityRepository;
        this.placeService = placeService;
        this.userService = userService;
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
    @PreAuthorize("hasRole(@role.USER)")
    @Transactional(readOnly = false)
    public PlaceRequest requestNewPlace(PlaceRequest placeRequest) {
        log.trace("Requesting new place: {}", placeRequest);

        placeRequest.setRequestingUser(userService.getCurrentUserUpdated().get());

        if (placeRequest.getPlace().getCity() != null) {
            final String cityIdentifier = placeRequest.getPlace().getCity().getIdentifier();
            final String stateIdentifier = placeRequest.getPlace().getCity().getState().getIdentifier();
            final Optional<City> city = cityRepository.findOneByStateIdentifierAndIdentifier(stateIdentifier, cityIdentifier);

            if (city.isPresent()) {
                log.trace("Found existing city attached to place request: {}", city.get());
                placeRequest.getPlace().setCity(city.get());
            } else {
                throw new ServiceException("Couldn't find city with identifier " + cityIdentifier);
            }
        }

        placeRequest.getPlace().setActive(false);
        placeRequest.setPlace(placeService.save(placeRequest.getPlace()));

        return placeRequestRepository.save(placeRequest);
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.MANAGE_PLACES)")
    @Transactional(readOnly = false)
    public PlaceRequest saveNewPlace(PlaceRequest placeRequest) {
        // TODO refactor...

        log.trace("Saving new place {}", placeRequest);

        final Optional<City> cityDb = cityRepository.findOneByStateIdentifierAndIdentifier(
                placeRequest.getPlace().getCity().getState().getIdentifier(),
                placeRequest.getPlace().getCity().getIdentifier());
        if (!cityDb.isPresent()) {
            throw new ServiceException("Found no city with identifier " + placeRequest.getPlace().getCity().getIdentifier());
        }
        if (!cityDb.get().equals(placeRequest.getPlace().getCity())) {
            throw new ServiceException("City in database differs from passed city!");
        }

        PlaceRequest placeRequestDb = placeRequestRepository.findOne(placeRequest.getId());
        if (placeRequestDb == null) {
            throw new ServiceException("Couldn't find place request with id " + placeRequest.getId());
        }

        final Optional<Place> place = placeService.load(placeRequest.getPlace().getCity().getState().getIdentifier(),
                placeRequest.getPlace().getCity().getIdentifier(),
                placeRequest.getPlace().getIdentifier());
        if (place.isPresent()) {
            if (!Objects.equals(place.get().getId(), placeRequestDb.getPlace().getId())) {
                throw new ServiceException("Already found an existing place with same identifier");
            }
        }

        placeRequestDb.getPlace().setCity(cityDb.get());
        placeRequestDb.getPlace().setActive(true);

        final Team team = new Team(placeRequestDb.getPlace());
        team.getMembers().add(placeRequestDb.getRequestingUser());
        placeRequestDb.getPlace().setTeam(team);

        // TODO notify user about accepting new place

        placeRequestDb = placeRequestRepository.save(placeRequestDb);

        Events.place(ChangeType.CREATE, placeRequestDb.getPlace()).publish();

        return placeRequestDb;
    }

    @Override
    @PreAuthorize("hasAuthority(@perm.MANAGE_PLACES)")
    @Transactional(readOnly = false)
    public void declinePlaceRequest(long placeRequestId) {
        log.debug("Declining place request with id {}", placeRequestId);

        final Optional<PlaceRequest> placeRequestDb = Optional.ofNullable(placeRequestRepository.findOne(placeRequestId));

        if (!placeRequestDb.isPresent()) {
            throw new ServiceException("Couldn't find place request to delete!");
        }
        if (placeRequestDb.get().getPlace().isActive()) {
            throw new ServiceException("Place is already active!");
        }

        placeRequestRepository.delete(placeRequestDb.get());
        placeService.deletePlace(placeRequestDb.get().getPlace().getId());
    }
}
