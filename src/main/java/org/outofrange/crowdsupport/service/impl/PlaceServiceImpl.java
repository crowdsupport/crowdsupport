package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.event.ChangeType;
import org.outofrange.crowdsupport.event.Events;
import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.persistence.PlaceRepository;
import org.outofrange.crowdsupport.persistence.TeamRepository;
import org.outofrange.crowdsupport.persistence.UserRepository;
import org.outofrange.crowdsupport.service.PlaceService;
import org.outofrange.crowdsupport.util.ServiceException;
import org.outofrange.crowdsupport.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {
    private static final Logger log = LoggerFactory.getLogger(PlaceServiceImpl.class);

    private final PlaceRepository placeRepository;
    private final DonationRequestRepository donationRequestRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Inject
    public PlaceServiceImpl(PlaceRepository placeRepository, DonationRequestRepository donationRequestRepository,
                            UserRepository userRepository, TeamRepository teamRepository) {
        this.placeRepository = placeRepository;
        this.donationRequestRepository = donationRequestRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional(readOnly = false)
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
        Validate.notNullOrEmpty(stateIdentifier);
        Validate.notNullOrEmpty(cityIdentifier);
        Validate.notNullOrEmpty(placeIdentifier);

        final Optional<Place> place = placeRepository.findOneByIdentifier(placeIdentifier);

        if (place.isPresent() && place.get().getCity() != null && cityIdentifier.equals(place.get().getCity().getIdentifier()) &&
                stateIdentifier.equals(place.get().getCity().getState().getIdentifier())) {
            return place;
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deletePlace(long id) {
        log.debug("Deleting place {}", id);
        placeRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = false)
    public Place addUserToTeam(long placeId, String username) {
        log.debug("Adding user {} to team of place with id {}", username, placeId);

        Validate.notNullOrEmpty(username);

        final Place placeDb = placeRepository.findOne(placeId);
        checkNull(placeDb, "Couldn't find place with id " + placeId);

        final User userDb = userRepository.findOneByUsername(username).get();

        if (!placeDb.getTeam().getMembers().contains(userDb)) {
            placeDb.getTeam().getMembers().add(userDb);

            // we don't need to save the whole place again
            placeDb.setTeam(teamRepository.save(placeDb.getTeam()));
        } else {
            log.debug("Nothing to do, user with username {} already in team of place {}", username, placeDb);
        }

        return placeDb;
    }

    @Override
    public Place loadPlace(long id) {
        log.debug("Loading place with id {}", id);

        return placeRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = false)
    public Place removeUserFromTeam(long placeId, String username) {
        log.debug("Removing user {} from team of place with id {}", username, placeId);

        Validate.notNullOrEmpty(username);

        final Place placeDb = placeRepository.findOne(placeId);
        checkNull(placeDb, "Couldn't find place with id " + placeId);

        final User userDb = userRepository.findOneByUsername(username).get();

        if (placeDb.getTeam().getMembers().remove(userDb)) {
            // we don't need to save the whole place again
            placeDb.setTeam(teamRepository.save(placeDb.getTeam()));
        } else {
            log.debug("Nothing to do, found no user with username {} in team of place {}", username, placeDb);
        }

        return placeDb;
    }

    @Override
    @Transactional(readOnly = false)
    public DonationRequest addDonationRequest(long placeId, DonationRequest donationRequest) {
        // TODO move to donation request service
        log.debug("Adding donation request {} to place {}", donationRequest, placeId);

        Validate.notNull(donationRequest);

        final Place placeDb = placeRepository.findOne(placeId);
        checkNull(placeDb, "Couldn't find place with id " + placeId);

        donationRequest.setPlace(placeDb);

        donationRequest = donationRequestRepository.save(donationRequest);

        Events.donationRequest(ChangeType.CREATE, donationRequest).publish();

        return donationRequest;
    }

    private void checkNull(Object object, String message) {
        if (object == null) {
            throw new ServiceException(message);
        }
    }
}
