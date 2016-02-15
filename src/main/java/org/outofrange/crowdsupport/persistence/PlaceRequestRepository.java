package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.PlaceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRequestRepository extends JpaRepository<PlaceRequest, Long> {
    /**
     * Loads all place requests associated with an inactive place.
     *
     * @return a list of place requests associated with an inactive place
     */
    List<PlaceRequest> findByPlaceActiveFalse();

    /**
     * Loads a place request associated with a place, found by its places identifier.
     *
     * @param identifier the identifier of the place
     * @return the found place request, or {@link Optional#empty()} if none could be found
     */
    Optional<PlaceRequest> findByPlaceIdentifier(String identifier);

    /**
     * Loads a place request by the identifiers of its place, city and state.
     *
     * @param placeIdentifier the identifier of the place
     * @param cityIdentifier  the identifier of the city
     * @param stateIdentifier the identifier of the state
     * @return the found place request, or {@link Optional#empty()} if none could be found
     */
    @Query("select pr from PlaceRequest pr where pr.place.identifier = ?1 " +
            "and pr.place.city.identifier = ?2 and pr.place.city.state.identifier = ?3")
    Optional<PlaceRequest> findPlaceByIdentifiers(String placeIdentifier, String cityIdentifier, String stateIdentifier);
}
