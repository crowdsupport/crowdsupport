package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.PlaceRequest;

import java.util.List;
import java.util.Optional;

public interface PlaceService {
    Optional<Place> load(String stateIdentifier, String cityIdentifier, String placeIdentifier);

    void deletePlace(Place place);

    Place addUserToTeam(long placeId, String username);

    Place loadPlace(long id);

    Place removeUserFromTeam(Long placeId, String username);

    DonationRequest addDonationRequest(Long placeId, DonationRequest donationRequest);

    Place save(Place entity);

    List<Place> loadAll();
}
