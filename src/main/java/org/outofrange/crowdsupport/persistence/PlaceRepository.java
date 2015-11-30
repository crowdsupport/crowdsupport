package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findOneByIdentifier(String identifier);
}
