package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    /**
     * Loads all cities where {@code namePart} is part of their name, ignoring case.
     *
     * @param namePart the text to look in names for.
     * @return a list of all matching cities.
     */
    List<City> findAllByNameContainingIgnoreCase(String namePart);

    /**
     * Loads a city having a specific identifier in a specific state.
     *
     * @param stateIdentifier the identifier of the state the city is in.
     * @param identifier      the identifier of the city.
     * @return the found city, or {@link Optional#empty()} if none is found.
     */
    Optional<City> findOneByStateIdentifierAndIdentifier(String stateIdentifier, String identifier);
}
