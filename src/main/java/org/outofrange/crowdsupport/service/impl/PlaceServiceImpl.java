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

    @Inject
    private DonationRequestRepository donationRequestRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TeamRepository teamRepository;

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
    public void deletePlace(Place place) {
        log.debug("Deleting place {}", place);
        placeRepository.delete(place);
    }

    @Override
    public Place addUserToTeam(long placeId, String username) {
        log.debug("Adding user {} to team of place with id {}", username, placeId);

        final Place placeDb = placeRepository.findOne(placeId);
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
    public Place removeUserFromTeam(Long placeId, String username) {
        log.debug("Removing user {} from team of place with id {}", username, placeId);

        final Place placeDb = placeRepository.findOne(placeId);
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
    public DonationRequest addDonationRequest(Long placeId, DonationRequest donationRequest) {
        // TODO move to donation request service
        log.debug("Adding donation request {} to place {}", donationRequest, placeId);

        final Place placeDb = placeRepository.findOne(placeId);
        donationRequest.setPlace(placeDb);

        donationRequest = donationRequestRepository.save(donationRequest);

        Events.place(placeDb).donationRequestChange(ChangeType.ADD, donationRequest).publish();

        return donationRequest;
    }
}
