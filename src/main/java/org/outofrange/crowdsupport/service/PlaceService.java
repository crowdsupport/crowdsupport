package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceService extends BaseService<Place> {
    Optional<Place> load(String stateIdentifier, String cityIdentifier, String placeIdentifier);

    List<Place> loadActivePlaces(String identifier);

    void deletePlace(Place place);

    void addUserToTeam(Place place, String username);

    void removeUserFromTeam(Place place, String username);
}
