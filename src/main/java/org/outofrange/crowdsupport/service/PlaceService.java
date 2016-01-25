package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;
import org.outofrange.crowdsupport.model.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceService extends BaseService<Place> {
    Optional<Place> load(String stateIdentifier, String cityIdentifier, String placeIdentifier);

    List<Place> loadActivePlaces(String identifier);

    void deletePlace(Place place);

    void addUserToTeam(Place place, String username);

    Place addUserToTeam(long placeId, String username);

    Place loadPlace(long id);

    void removeUserFromTeam(Place place, String username);

    Place removeUserFromTeam(Long placeId, String username);

    DonationRequest addDonationRequest(Long placeId, DonationRequest donationRequest);
}
