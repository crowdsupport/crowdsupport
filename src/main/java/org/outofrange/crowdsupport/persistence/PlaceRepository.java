package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    /**
     * Returns a place found by its identifier.
     *
     * @param identifier the identifier of the place
     * @return the loaded place, or {@link Optional#empty()} if none could be found
     */
    Optional<Place> findOneByIdentifier(String identifier);

    /**
     * Returns a list of active places in a city identified by a given string.
     *
     * @param cityIdentifier the identifier of the city
     * @return a list of active places found in the city
     */
    List<Place> findByActiveTrueAndCityIdentifier(String cityIdentifier);

    /**
     * Returns a list of places who have a given text part anywhere in the name or identifier of them, their city or
     * their state.
     * <p>
     * Ignores casing.
     *
     * @param text the text to look for
     * @return a list of places matching the criteria
     */
    @Query("SELECT p FROM Place p WHERE UPPER(p.name) LIKE %?1% OR UPPER(p.identifier) LIKE %?1% OR" +
            " UPPER(p.city.name) LIKE %?1% OR UPPER(p.city.identifier) LIKE %?1% OR" +
            " UPPER(p.city.state.name) LIKE %?1% OR UPPER(p.city.state.identifier) LIKE %?1%")
    List<Place> findPlacesRelatedToText(String text);
}
