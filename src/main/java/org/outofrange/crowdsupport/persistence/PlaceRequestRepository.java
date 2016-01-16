package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.PlaceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRequestRepository extends JpaRepository<PlaceRequest, Long> {
    List<PlaceRequest> findByPlaceActiveFalse();

    Optional<PlaceRequest> findByPlaceIdentifier(String identifier);

    @Query("select pr from PlaceRequest pr where pr.place.identifier = ?1 and pr.place.city.identifier = ?2 and pr.place.city.state.identifier = ?3")
    Optional<PlaceRequest> findPlaceByIdentifiers(String placeIdentifier, String cityIdentifier, String stateIdentifier);
}
