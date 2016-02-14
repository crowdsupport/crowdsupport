package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findOneByIdentifier(String identifier);

    List<Place> findByActiveTrueAndCityIdentifier(String cityIdentifier);

    @Query("SELECT p FROM Place p WHERE UPPER(p.name) LIKE %?1% OR UPPER(p.identifier) LIKE %?1% OR" +
            " UPPER(p.city.name) LIKE %?1% OR UPPER(p.city.identifier) LIKE %?1% OR" +
            " UPPER(p.city.state.name) LIKE %?1% OR UPPER(p.city.state.identifier) LIKE %?1%")
    List<Place> findPlacesRelatedToText(String text);
}
