package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceService {
    Optional<Place> load(String stateIdentifier, String cityIdentifier, String placeIdentifier);

    void deletePlace(long id);

    Place addUserToTeam(long placeId, String username);

    Place loadPlace(long id);

    Place removeUserFromTeam(long placeId, String username);

    DonationRequest addDonationRequest(long placeId, DonationRequest donationRequest);

    Place save(Place entity);

    List<Place> loadAll();
}
