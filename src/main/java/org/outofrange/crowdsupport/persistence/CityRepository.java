package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findOneByIdentifier(String identifier);

    List<City> findAllByNameContainingIgnoreCase(String namePart);

    Optional<City> findOneByStateIdentifierAndIdentifier(String stateIdentifier, String identifier);
}
