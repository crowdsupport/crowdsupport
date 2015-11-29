package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
